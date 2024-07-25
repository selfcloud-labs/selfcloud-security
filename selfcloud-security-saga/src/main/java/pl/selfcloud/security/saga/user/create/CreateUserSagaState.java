package pl.selfcloud.security.saga.user.create;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.common.reply.CustomerCreatedReply;
import pl.selfcloud.customer.api.saga.command.CreateCustomerCommand;
import pl.selfcloud.customer.api.saga.command.DeleteCustomerCommand;
import pl.selfcloud.security.api.dto.RegistrationDto;
import pl.selfcloud.security.api.saga.command.ApproveUserCommand;
import pl.selfcloud.security.api.saga.command.RejectUserCommand;
import pl.selfcloud.security.api.state.RejectionReason;
import pl.selfcloud.security.api.state.UserState;

@Data
@NoArgsConstructor
public class CreateUserSagaState {

  private RegistrationDto registrationDto;
  private Long userId;
  private RejectionReason rejectionReason;
  private UserState userState;
  private Long customerId;
//  private CompletableFuture<UserState> sagaCompletionFuture;
  public CreateUserSagaState(final Long userId, final RegistrationDto registrationDto){
    this.userId = userId;
    this.registrationDto = registrationDto;
    this.userState = UserState.PENDING;
//    this.sagaCompletionFuture = new CompletableFuture<>();
  }
  RejectUserCommand makeRejectUserCommand() {
    return new RejectUserCommand(getUserId());
  }

  CreateCustomerCommand makeCreateCustomerCommand(){
    return new CreateCustomerCommand(getUserId(), getRegistrationDto().getCustomerDto());
  }

  void handleCreateCustomerCommand(CustomerCreatedReply reply){
    setCustomerId(reply.getCustomerId());
  }

  ApproveUserCommand makeApproveUserCommand() {
//    System.out.println("User state in makeApproveUserCommand: " + UserState.READY);
//    this.sagaCompletionFuture.complete(UserState.READY);
    return new ApproveUserCommand(getUserId());
  }

  DeleteCustomerCommand deleteCustomerCommand() {
    return new DeleteCustomerCommand(getCustomerId());
  }
}