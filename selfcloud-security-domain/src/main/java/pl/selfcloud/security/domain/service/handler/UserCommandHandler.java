package pl.selfcloud.security.domain.service.handler;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.selfcloud.security.api.saga.command.ApproveUserCommand;
import pl.selfcloud.security.api.saga.command.RejectUserCommand;
import pl.selfcloud.security.api.state.RejectionReason;
import pl.selfcloud.security.domain.model.User;
import pl.selfcloud.security.domain.repository.UserRepository;

@AllArgsConstructor
@Component
public class UserCommandHandler {

  @Autowired
  private final UserRepository userRepository;

  public CommandHandlers commandHandlersDefinition(){
    return SagaCommandHandlersBuilder
        .fromChannel("userService")
        .onMessage(ApproveUserCommand.class, this::approve)
        .onMessage(RejectUserCommand.class, this::reject)
        .build();
  }

  public Message approve(CommandMessage<ApproveUserCommand> cm) {
    long userId = cm.getCommand().getUserId();
    User user = userRepository.findById(userId).get();
    user.approve();
    return withSuccess();
  }

  public Message reject(CommandMessage<RejectUserCommand> cm) {
    long userId = cm.getCommand().getUserId();
    User user = userRepository.findById(userId).get();
    user.reject(RejectionReason.UNKNOWN_CUSTOMER);
    return withSuccess();
  }
}
