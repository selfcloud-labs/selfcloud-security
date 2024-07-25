package pl.selfcloud.security.saga.user.create.proxy;

import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import pl.selfcloud.security.api.saga.command.ApproveUserCommand;
import pl.selfcloud.security.api.saga.command.RejectUserCommand;

public class UserServiceProxy {

  public final CommandEndpoint<RejectUserCommand> reject = CommandEndpointBuilder
      .forCommand(RejectUserCommand.class)
      .withChannel("userService")
      .withReply(Success.class)
      .build();

  public final CommandEndpoint<ApproveUserCommand> approve = CommandEndpointBuilder
      .forCommand(ApproveUserCommand.class)
      .withChannel("userService")
      .withReply(Success.class)
      .build();


}
