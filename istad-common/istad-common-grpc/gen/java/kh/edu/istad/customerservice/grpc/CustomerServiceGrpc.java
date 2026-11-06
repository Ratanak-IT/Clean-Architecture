package kh.edu.istad.customerservice.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.0)",
    comments = "Source: customer.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CustomerServiceGrpc {

  private CustomerServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "customer.CustomerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetCustomerRequest,
      kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetCustomerByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCustomerById",
      requestType = kh.edu.istad.customerservice.grpc.GetCustomerRequest.class,
      responseType = kh.edu.istad.customerservice.grpc.CustomerGetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetCustomerRequest,
      kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetCustomerByIdMethod() {
    io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetCustomerRequest, kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetCustomerByIdMethod;
    if ((getGetCustomerByIdMethod = CustomerServiceGrpc.getGetCustomerByIdMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getGetCustomerByIdMethod = CustomerServiceGrpc.getGetCustomerByIdMethod) == null) {
          CustomerServiceGrpc.getGetCustomerByIdMethod = getGetCustomerByIdMethod =
              io.grpc.MethodDescriptor.<kh.edu.istad.customerservice.grpc.GetCustomerRequest, kh.edu.istad.customerservice.grpc.CustomerGetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCustomerById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.customerservice.grpc.GetCustomerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.customerservice.grpc.CustomerGetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("GetCustomerById"))
              .build();
        }
      }
    }
    return getGetCustomerByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetAllCustomerRequest,
      kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetAllCustomerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllCustomer",
      requestType = kh.edu.istad.customerservice.grpc.GetAllCustomerRequest.class,
      responseType = kh.edu.istad.customerservice.grpc.CustomerGetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetAllCustomerRequest,
      kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetAllCustomerMethod() {
    io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetAllCustomerRequest, kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetAllCustomerMethod;
    if ((getGetAllCustomerMethod = CustomerServiceGrpc.getGetAllCustomerMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getGetAllCustomerMethod = CustomerServiceGrpc.getGetAllCustomerMethod) == null) {
          CustomerServiceGrpc.getGetAllCustomerMethod = getGetAllCustomerMethod =
              io.grpc.MethodDescriptor.<kh.edu.istad.customerservice.grpc.GetAllCustomerRequest, kh.edu.istad.customerservice.grpc.CustomerGetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllCustomer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.customerservice.grpc.GetAllCustomerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.customerservice.grpc.CustomerGetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("GetAllCustomer"))
              .build();
        }
      }
    }
    return getGetAllCustomerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest,
      kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetCustomerByUsernameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCustomerByUsername",
      requestType = kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest.class,
      responseType = kh.edu.istad.customerservice.grpc.CustomerGetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest,
      kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetCustomerByUsernameMethod() {
    io.grpc.MethodDescriptor<kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest, kh.edu.istad.customerservice.grpc.CustomerGetResponse> getGetCustomerByUsernameMethod;
    if ((getGetCustomerByUsernameMethod = CustomerServiceGrpc.getGetCustomerByUsernameMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getGetCustomerByUsernameMethod = CustomerServiceGrpc.getGetCustomerByUsernameMethod) == null) {
          CustomerServiceGrpc.getGetCustomerByUsernameMethod = getGetCustomerByUsernameMethod =
              io.grpc.MethodDescriptor.<kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest, kh.edu.istad.customerservice.grpc.CustomerGetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCustomerByUsername"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.customerservice.grpc.CustomerGetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("GetCustomerByUsername"))
              .build();
        }
      }
    }
    return getGetCustomerByUsernameMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CustomerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerServiceStub>() {
        @java.lang.Override
        public CustomerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerServiceStub(channel, callOptions);
        }
      };
    return CustomerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CustomerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerServiceBlockingStub>() {
        @java.lang.Override
        public CustomerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerServiceBlockingStub(channel, callOptions);
        }
      };
    return CustomerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CustomerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerServiceFutureStub>() {
        @java.lang.Override
        public CustomerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerServiceFutureStub(channel, callOptions);
        }
      };
    return CustomerServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getCustomerById(kh.edu.istad.customerservice.grpc.GetCustomerRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCustomerByIdMethod(), responseObserver);
    }

    /**
     */
    default void getAllCustomer(kh.edu.istad.customerservice.grpc.GetAllCustomerRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllCustomerMethod(), responseObserver);
    }

    /**
     */
    default void getCustomerByUsername(kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCustomerByUsernameMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CustomerService.
   */
  public static abstract class CustomerServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CustomerServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CustomerService.
   */
  public static final class CustomerServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CustomerServiceStub> {
    private CustomerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerServiceStub(channel, callOptions);
    }

    /**
     */
    public void getCustomerById(kh.edu.istad.customerservice.grpc.GetCustomerRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCustomerByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllCustomer(kh.edu.istad.customerservice.grpc.GetAllCustomerRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getGetAllCustomerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCustomerByUsername(kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCustomerByUsernameMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CustomerService.
   */
  public static final class CustomerServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CustomerServiceBlockingStub> {
    private CustomerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public kh.edu.istad.customerservice.grpc.CustomerGetResponse getCustomerById(kh.edu.istad.customerservice.grpc.GetCustomerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCustomerByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<kh.edu.istad.customerservice.grpc.CustomerGetResponse> getAllCustomer(
        kh.edu.istad.customerservice.grpc.GetAllCustomerRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getGetAllCustomerMethod(), getCallOptions(), request);
    }

    /**
     */
    public kh.edu.istad.customerservice.grpc.CustomerGetResponse getCustomerByUsername(kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCustomerByUsernameMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CustomerService.
   */
  public static final class CustomerServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CustomerServiceFutureStub> {
    private CustomerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<kh.edu.istad.customerservice.grpc.CustomerGetResponse> getCustomerById(
        kh.edu.istad.customerservice.grpc.GetCustomerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCustomerByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<kh.edu.istad.customerservice.grpc.CustomerGetResponse> getCustomerByUsername(
        kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCustomerByUsernameMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CUSTOMER_BY_ID = 0;
  private static final int METHODID_GET_ALL_CUSTOMER = 1;
  private static final int METHODID_GET_CUSTOMER_BY_USERNAME = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_CUSTOMER_BY_ID:
          serviceImpl.getCustomerById((kh.edu.istad.customerservice.grpc.GetCustomerRequest) request,
              (io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_CUSTOMER:
          serviceImpl.getAllCustomer((kh.edu.istad.customerservice.grpc.GetAllCustomerRequest) request,
              (io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse>) responseObserver);
          break;
        case METHODID_GET_CUSTOMER_BY_USERNAME:
          serviceImpl.getCustomerByUsername((kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest) request,
              (io.grpc.stub.StreamObserver<kh.edu.istad.customerservice.grpc.CustomerGetResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetCustomerByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              kh.edu.istad.customerservice.grpc.GetCustomerRequest,
              kh.edu.istad.customerservice.grpc.CustomerGetResponse>(
                service, METHODID_GET_CUSTOMER_BY_ID)))
        .addMethod(
          getGetAllCustomerMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              kh.edu.istad.customerservice.grpc.GetAllCustomerRequest,
              kh.edu.istad.customerservice.grpc.CustomerGetResponse>(
                service, METHODID_GET_ALL_CUSTOMER)))
        .addMethod(
          getGetCustomerByUsernameMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              kh.edu.istad.customerservice.grpc.GetCustomerByUsernameRequest,
              kh.edu.istad.customerservice.grpc.CustomerGetResponse>(
                service, METHODID_GET_CUSTOMER_BY_USERNAME)))
        .build();
  }

  private static abstract class CustomerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CustomerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return kh.edu.istad.customerservice.grpc.Customer.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CustomerService");
    }
  }

  private static final class CustomerServiceFileDescriptorSupplier
      extends CustomerServiceBaseDescriptorSupplier {
    CustomerServiceFileDescriptorSupplier() {}
  }

  private static final class CustomerServiceMethodDescriptorSupplier
      extends CustomerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CustomerServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CustomerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CustomerServiceFileDescriptorSupplier())
              .addMethod(getGetCustomerByIdMethod())
              .addMethod(getGetAllCustomerMethod())
              .addMethod(getGetCustomerByUsernameMethod())
              .build();
        }
      }
    }
    return result;
  }
}
