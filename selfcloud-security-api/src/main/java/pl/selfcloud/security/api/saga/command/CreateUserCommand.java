package pl.selfcloud.security.api.saga.command;

import io.eventuate.tram.commands.common.Command;

public class CreateUserCommand implements Command {
  private long userId;

  private CreateUserCommand() {
  }


  public CreateUserCommand(long userId) {

    this.userId = userId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }
}
