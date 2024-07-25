package pl.selfcloud.security.infrastructure.config;

//@Configuration
//@Import({TramEventsPublisherConfiguration.class, SagaOrchestratorConfiguration.class, TramJdbcKafkaConfiguration.class})
public class UserSagaConfig {

//  @Bean
//  public CreateUserSaga createUserSaga(DomainEventPublisher domainEventPublisher,UserServiceProxy userService, CustomerServiceProxy customerServiceProxy, MessageProducer messageProducer){
//    return new CreateUserSaga(userService, customerServiceProxy);
//  }
//  @Bean
//  public UserService userService(
//      UserRepository userRepository,
//    RoleRepository roleRepository,
//    PasswordEncoder passwordEncoder,
//    AuthenticationManager authenticationManager,
//      UserDomainEventPublisher eventPublisher,
//    SagaManager<CreateUserSagaState> sagaManager,
//      JwtUtil jwtUtil,
//      CreateUserSagaCompletionListener sagaCompletionListener
//  ){
//    return new UserService(userRepository, roleRepository, passwordEncoder, authenticationManager, eventPublisher, sagaManager, jwtUtil, sagaCompletionListener);
//  }
//
//  @Bean
//  public SagaManager<CreateUserSagaState> createUserSagaStateSagaManager(CreateUserSaga saga,
//      SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer,
//      MessageConsumer messageConsumer, SagaLockManager sagaLockManager,
//      SagaCommandProducer sagaCommandProducer){
//    return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer,
//        sagaLockManager, sagaCommandProducer);
//  }
//
//  @Bean
//  public CreateUserSaga createUserSaga(UserServiceProxy userServiceProxy, CustomerServiceProxy customerServiceProxy){
//    return new CreateUserSaga(userServiceProxy, customerServiceProxy);
//  }
//
//  @Bean
//  public CommandDispatcher userCommandDispatcher(UserCommandHandler target, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
//    return sagaCommandDispatcherFactory.make("userService", target.commandHandlersDefinition());
//  }
//  @Bean
//  public CustomerServiceProxy customerServiceProxy() {
//    return new CustomerServiceProxy();
//  }
//
//  @Bean
//  public UserServiceProxy userServiceProxy(){
//    return new UserServiceProxy();
//  }
//
////  @Bean
////  public UserDomainEventPublisher userAggregateEventPublisher(DomainEventPublisher eventPublisher) {
////    return new UserDomainEventPublisher(eventPublisher);
////  }

}
