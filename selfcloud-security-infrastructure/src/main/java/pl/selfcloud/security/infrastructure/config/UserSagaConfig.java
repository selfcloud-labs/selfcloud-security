package pl.selfcloud.security.infrastructure.config;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.sagas.common.SagaLockManager;
import io.eventuate.tram.sagas.orchestration.SagaCommandProducer;
import io.eventuate.tram.sagas.orchestration.SagaInstanceRepository;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.selfcloud.common.security.util.JwtUtil;
import pl.selfcloud.security.domain.repository.RoleRepository;
import pl.selfcloud.security.domain.repository.UserRepository;
import pl.selfcloud.security.domain.service.UserService;
import pl.selfcloud.security.domain.service.handler.UserCommandHandler;
import pl.selfcloud.security.domain.service.publisher.UserDomainEventPublisher;
import pl.selfcloud.security.saga.user.create.CreateUserSaga;
import pl.selfcloud.security.saga.user.create.CreateUserSagaState;
import pl.selfcloud.security.saga.user.create.proxy.CustomerServiceProxy;
import pl.selfcloud.security.saga.user.create.proxy.UserServiceProxy;


@Configuration
@Import({TramEventsPublisherConfiguration.class, SagaOrchestratorConfiguration.class, TramJdbcKafkaConfiguration.class})
public class UserSagaConfig {

  //  @Bean
//  public CreateUserSaga createUserSaga(DomainEventPublisher domainEventPublisher,UserServiceProxy userService, CustomerServiceProxy customerServiceProxy, MessageProducer messageProducer){
//    return new CreateUserSaga(userService, customerServiceProxy);
//  }
  @Bean
  public UserService userService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      UserDomainEventPublisher eventPublisher,
      SagaManager<CreateUserSagaState> sagaManager,
      JwtUtil jwtUtil
//      CreateUserSagaCompletionListener sagaCompletionListener
  ){
    return new UserService(userRepository, roleRepository, passwordEncoder, authenticationManager,
        eventPublisher,
        sagaManager, jwtUtil
//        ,
//        sagaCompletionListener
    );
  }

  @Bean
  public SagaManager<CreateUserSagaState> createUserSagaStateSagaManager(CreateUserSaga saga,
      SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer,
      MessageConsumer messageConsumer, SagaLockManager sagaLockManager,
      SagaCommandProducer sagaCommandProducer){
    return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer,
        sagaLockManager, sagaCommandProducer);
  }

  @Bean
  public CreateUserSaga createUserSaga(UserServiceProxy userServiceProxy, CustomerServiceProxy customerServiceProxy){
    return new CreateUserSaga(userServiceProxy, customerServiceProxy);
  }

  @Bean
  public CommandDispatcher userCommandDispatcher(UserCommandHandler target, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
    return sagaCommandDispatcherFactory.make("userService", target.commandHandlersDefinition());
  }
  @Bean
  public CustomerServiceProxy customerServiceProxy() {
    return new CustomerServiceProxy();
  }

  @Bean
  public UserServiceProxy userServiceProxy(){
    return new UserServiceProxy();
  }

//  @Bean
//  public UserDomainEventPublisher userAggregateEventPublisher(DomainEventPublisher eventPublisher) {
//    return new UserDomainEventPublisher(eventPublisher);
//  }

}
