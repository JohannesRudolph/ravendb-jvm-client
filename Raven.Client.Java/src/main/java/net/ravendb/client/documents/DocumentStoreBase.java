package net.ravendb.client.documents;

import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.indexes.AbstractIndexCreationTask;
import net.ravendb.client.documents.indexes.IndexCreation;
import net.ravendb.client.documents.indexes.IndexDefinition;
import net.ravendb.client.documents.operations.AdminOperationExecutor;
import net.ravendb.client.documents.operations.OperationExecutor;
import net.ravendb.client.documents.operations.indexes.PutIndexesOperation;
import net.ravendb.client.documents.session.*;
import net.ravendb.client.http.RequestExecutor;
import net.ravendb.client.primitives.EventHandler;
import net.ravendb.client.primitives.VoidArgs;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *  Contains implementation of some IDocumentStore operations shared by DocumentStore implementations
 */
public abstract class DocumentStoreBase implements IDocumentStore {

    private List<EventHandler<BeforeStoreEventArgs>> onBeforeStore = new ArrayList<>();
    private List<EventHandler<AfterStoreEventArgs>> onAfterStore = new ArrayList<>();
    private List<EventHandler<BeforeDeleteEventArgs>> onBeforeDelete = new ArrayList<>();
    private List<EventHandler<BeforeQueryExecutedEventArgs>> onBeforeQueryExecuted = new ArrayList<>();

    protected DocumentStoreBase() {
        //TBD: Subscriptions = new DocumentSubscriptions(this);
    }

    public abstract void close();

    public abstract void addAfterCloseListener(EventHandler<VoidArgs> event);

    public abstract void removeAfterCloseListener(EventHandler<VoidArgs> event);

    protected boolean disposed;

    public boolean isDisposed() {
        return disposed;
    }

    //TODO: public abstract IDisposable AggressivelyCacheFor(TimeSpan cacheDuration, string database = null);

    //TBD: public abstract IDatabaseChanges Changes(string database = null);

    //TODO: public abstract IDisposable DisableAggressiveCaching(string database = null);

    public abstract String getIdentifier();

    public abstract void setIdentifier(String identifier);

    public abstract IDocumentStore initialize();

    public abstract IDocumentSession openSession();

    public abstract IDocumentSession openSession(String database);

    public abstract IDocumentSession openSession(SessionOptions sessionOptions);

    public void executeIndex(AbstractIndexCreationTask task) {
        assertInitialized();
        task.execute(this, conventions);
    }

    @Override
    public void executeIndexes(List<AbstractIndexCreationTask> tasks) {
        assertInitialized();
        IndexDefinition[] indexesToAdd = IndexCreation.createIndexesToAdd(tasks, conventions);

        admin().send(new PutIndexesOperation(indexesToAdd));
    }

    private DocumentConventions conventions;

    /**
     * Gets the conventions.
     */
    @Override
    public DocumentConventions getConventions() {
        if (conventions == null) {
            conventions = new DocumentConventions();
        }
        return conventions;
    }

    public void setConventions(DocumentConventions conventions) {
        this.conventions = conventions;
    }

    protected String[] urls = new String[0];

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] value) {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }

        for (int i = 0; i < value.length; i++) {
            if (value[i] == null)
                throw new IllegalArgumentException("Urls cannot contain null");

            try {
                new URL(value[i]);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("The url '" + value[i] + "' is not valid");
            }

            value[i] = StringUtils.stripEnd(value[i], "/");
        }

        this.urls = value;
    }

    protected boolean initialized;

    //TBD:  private X509Certificate2 _certificate;
    //TBD: public abstract BulkInsertOperation BulkInsert(string database = null);
    //TBD:  public IReliableSubscriptions Subscriptions { get; }

    protected void ensureNotClosed() {
        if (disposed) {
            throw new IllegalStateException("The document store has already been disposed and cannot be used");
        }
    }

    protected void assertInitialized() {
        if (!initialized) {
            throw new IllegalStateException("You cannot open a session or access the database commands before initializing the document store. Did you forget calling initialize()?");
        }
    }
    /* TODO
     protected virtual void AfterSessionCreated(InMemoryDocumentSessionOperations session)
        {
            var onSessionCreatedInternal = SessionCreatedInternal;
            onSessionCreatedInternal?.Invoke(session);
        }
        public event Action<InMemoryDocumentSessionOperations> SessionCreatedInternal;
        public event Action<string> TopologyUpdatedInternal;
     */

    public void addBeforeStoreListener(EventHandler<BeforeStoreEventArgs> handler) {
        this.onBeforeStore.add(handler);

    }
    public void removeBeforeStoreListener(EventHandler<BeforeStoreEventArgs> handler) {
        this.onBeforeStore.remove(handler);
    }

    public void addAfterStoreListener(EventHandler<AfterStoreEventArgs> handler) {
        this.onAfterStore.add(handler);
    }

    public void removeAfterStoreListener(EventHandler<AfterStoreEventArgs> handler) {
        this.onAfterStore.remove(handler);
    }

    public void addBeforeDeleteListener(EventHandler<BeforeDeleteEventArgs> handler) {
        this.onBeforeDelete.add(handler);
    }
    public void removeBeforeDeleteListener(EventHandler<BeforeDeleteEventArgs> handler) {
        this.onBeforeDelete.remove(handler);
    }

    public void addBeforeQueryExecutedListener(EventHandler<BeforeQueryExecutedEventArgs> handler) {
        this.onBeforeQueryExecuted.add(handler);
    }
    public void removeBeforeQueryExecutedListener(EventHandler<BeforeQueryExecutedEventArgs> handler) {
        this.onBeforeQueryExecuted.remove(handler);
    }

    protected String database;

    /**
     * Gets the default database
     */
    @Override
    public String getDatabase() {
        return database;
    }

    /**
     * Sets the default database
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /* TBD
     /// <summary>
        /// The client certificate to use for authentication
        /// </summary>
        public X509Certificate2 Certificate
        {
            get => _certificate;
            set
            {
                if(Initialized)
                    throw new InvalidOperationException("You cannot change the certificate after the document store was initialized");
                _certificate = value;
            }
        }
     */

    public abstract RequestExecutor getRequestExecutor();

    public abstract RequestExecutor getRequestExecutor(String databaseName);

    /* TODO

        public abstract IDisposable SetRequestsTimeout(TimeSpan timeout, string database = null);

        /// <summary>
        /// Setup the context for aggressive caching.
        /// </summary>
        public IDisposable AggressivelyCache(string database = null)
        {
            return AggressivelyCacheFor(TimeSpan.FromDays(1), database);
        }

        */

    protected void registerEvents(InMemoryDocumentSessionOperations session) {
        for (EventHandler<BeforeStoreEventArgs> handler : onBeforeStore) {
            session.addBeforeStoreListener(handler);
        }

        for (EventHandler<AfterStoreEventArgs> handler : onAfterStore) {
            session.addAfterStoreListener(handler);
        }

        for (EventHandler<BeforeDeleteEventArgs> handler : onBeforeDelete) {
            session.addBeforeDeleteListener(handler);
        }

        for (EventHandler<BeforeQueryExecutedEventArgs> handler : onBeforeQueryExecuted) {
            session.addBeforeQueryExecutedListener(handler);
        }

    }

    public abstract AdminOperationExecutor admin();

    public abstract OperationExecutor operations();

    /* TODO
        protected void OnTopologyUpdatedInternal(string databaseName)
        {
            TopologyUpdatedInternal?.Invoke(databaseName);
        }
     */
}
