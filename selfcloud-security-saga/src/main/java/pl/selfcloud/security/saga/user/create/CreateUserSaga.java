package pl.selfcloud.security.saga.user.create;

import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import pl.selfcloud.customer.api.saga.reply.CustomerCreatedReply;
import pl.selfcloud.security.api.event.CreateUSerSagaCompletedSuccessfullyEvent;
import pl.selfcloud.security.api.event.CreateUserSagaRolledBack;
import pl.selfcloud.security.saga.user.create.proxy.CustomerServiceProxy;
import pl.selfcloud.security.saga.user.create.proxy.UserServiceProxy;
import pl.selfcloud.security.saga.user.create.publisher.UserDomainSagaPublisher;


public class CreateUserSaga implements SimpleSaga<CreateUserSagaState>{

  private final SagaDefinition<CreateUserSagaState> sagaDefinition;
  @Autowired
  private UserDomainSagaPublisher userDomainSagaPublisher;
  @Autowired
  public CreateUserSaga(final UserServiceProxy userServiceProxy,final CustomerServiceProxy customerServiceProxy) {

    this.sagaDefinition =
      step()
        .withCompensation(userServiceProxy.reject, CreateUserSagaState::makeRejectUserCommand)
      .step()
        .invokeParticipant(customerServiceProxy.createCustomer, CreateUserSagaState::makeCreateCustomerCommand)
        .onReply(CustomerCreatedReply.class, CreateUserSagaState::handleCreateCustomerCommand)
        .withCompensation(customerServiceProxy.deleteCustomer, CreateUserSagaState::deleteCustomerCommand)
      .step()
        .invokeParticipant(userServiceProxy.approve, CreateUserSagaState::makeApproveUserCommand)
      .build();
  }

  @Override
  public SagaDefinition<CreateUserSagaState> getSagaDefinition(){
    return this.sagaDefinition;
  }

  @Override
  public void onSagaCompletedSuccessfully(String sagaId, CreateUserSagaState createUserSagaState) {
    userDomainSagaPublisher.publish(createUserSagaState, Collections.singletonList(new CreateUSerSagaCompletedSuccessfullyEvent(createUserSagaState.getUserId(), "Saga completed successfully")));
  }

  @Override
  public void onSagaRolledBack(String sagaId, CreateUserSagaState createUserSagaState) {
    userDomainSagaPublisher.publish(createUserSagaState, Collections.singletonList(new CreateUserSagaRolledBack(createUserSagaState.getUserId(), "Saga rolled back.")));
  }
}
