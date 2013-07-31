package raven.client.document;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import raven.abstractions.closure.Action1;
import raven.abstractions.closure.Function1;
import raven.abstractions.closure.Function3;
import raven.abstractions.closure.Functions;
import raven.client.connection.IDatabaseCommands;
import raven.client.connection.ReplicationInformer;

// TODO: finish me
public class DocumentConvention {

  private AtomicInteger requestCount = new AtomicInteger(0);

  private EnumSet<FailoverBehavior> failoverBehavior = EnumSet.of(FailoverBehavior.FAIL_IMMEDIATELY);

  private Function1<String, Boolean> shouldCacheRequest;

  private Function1<HttpResponse, Action1<HttpRequest>> handleForbiddenResponse;

  private Function1<HttpResponse, Action1<HttpRequest>> handleUnauthorizedResponse;

  private Function3<String, IDatabaseCommands, Object, String> documentKeyGenerator;


  public Function3<String, IDatabaseCommands, Object, String> getDocumentKeyGenerator() {
    return documentKeyGenerator;
  }


  public void setDocumentKeyGenerator(Function3<String, IDatabaseCommands, Object, String> documentKeyGenerator) {
    this.documentKeyGenerator = documentKeyGenerator;
  }

  /* The maximum amount of time that we will wait before checking
   * that a failed node is still up or not.
   * Default: 5 minutes */
  private long maxFailoverCheckPeriod = 300000;


  public long getMaxFailoverCheckPeriod() {
    return maxFailoverCheckPeriod;
  }


  public void setMaxFailoverCheckPeriod(long maxFailoverCheckPeriod) {
    this.maxFailoverCheckPeriod = maxFailoverCheckPeriod;
  }

  private boolean enlistInDistributedTransactions = false;


  /**
   * @return the handleUnauthorizedResponse
   */
  public Function1<HttpResponse, Action1<HttpRequest>> getHandleUnauthorizedResponse() {
    return handleUnauthorizedResponse;
  }

  /**
   * @param handleUnauthorizedResponse the handleUnauthorizedResponse to set
   */
  public void setHandleUnauthorizedResponse(Function1<HttpResponse, Action1<HttpRequest>> handleUnauthorizedResponse) {
    this.handleUnauthorizedResponse = handleUnauthorizedResponse;
  }

  /**
   * @return the handleForbiddenResponse
   */
  public Function1<HttpResponse, Action1<HttpRequest>> getHandleForbiddenResponse() {
    return handleForbiddenResponse;
  }

  /**
   * @param handleForbiddenResponse the handleForbiddenResponse to set
   */
  public void setHandleForbiddenResponse(Function1<HttpResponse, Action1<HttpRequest>> handleForbiddenResponse) {
    this.handleForbiddenResponse = handleForbiddenResponse;
  }

  public DocumentConvention() {
    //TODO:
    shouldCacheRequest = Functions.alwaysTrue();
  }

  /**
   * @return the shouldCacheRequest
   */
  public Function1<String, Boolean> getShouldCacheRequest() {
    return shouldCacheRequest;
  }

  public Boolean shouldCacheRequest(String url) {
    return shouldCacheRequest.apply(url);
  }

  /**
   * @param shouldCacheRequest the shouldCacheRequest to set
   */
  public void setShouldCacheRequest(Function1<String, Boolean> shouldCacheRequest) {
    this.shouldCacheRequest = shouldCacheRequest;
  }

  /**
   * @return the failoverBehavior
   */
  public EnumSet<FailoverBehavior> getFailoverBehavior() {
    return failoverBehavior;
  }

  /**
   * @param failoverBehavior the failoverBehavior to set
   */
  public void setFailoverBehavior(EnumSet<FailoverBehavior> failoverBehavior) {
    this.failoverBehavior = failoverBehavior;
  }

  public int incrementRequestCount() {
    return requestCount.incrementAndGet();
  }

  public EnumSet<FailoverBehavior> getFailoverBehaviorWithoutFlags() {
    EnumSet<FailoverBehavior> result = this.failoverBehavior.clone();
    result.remove(FailoverBehavior.READ_FROM_ALL_SERVERS);
    return result;
  }

  public boolean isUseParallelMultiGet() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isDisableProfiling() {
    // TODO Auto-generated method stub
    return false;
  }

  public void handleForbiddenResponse(HttpResponse forbiddenResponse) {
    handleForbiddenResponse.apply(forbiddenResponse);
  }

  public Action1<HttpRequest> handleUnauthorizedResponse(HttpResponse unauthorizedResponse) {
    return handleUnauthorizedResponse.apply(unauthorizedResponse);
  }

  public boolean isEnlistInDistributedTransactions() {
    return enlistInDistributedTransactions;
  }

  public void setEnlistInDistributedTransactions(boolean enlistInDistributedTransactions) {
    this.enlistInDistributedTransactions = enlistInDistributedTransactions;
  }


  public void setDisableProfiling(boolean b) {
    // TODO Auto-generated method stub

  }


  public ReplicationInformer getReplicationInformerFactory() {
    // TODO Auto-generated method stub
    return null;
  }


}
