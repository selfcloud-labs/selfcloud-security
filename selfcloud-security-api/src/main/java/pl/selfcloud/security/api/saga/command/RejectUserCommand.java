package pl.selfcloud.security.api.saga.command;

import io.eventuate.tram.commands.common.Command;

public class RejectUserCommand implements Command {

  private long userId;

  private RejectUserCommand() {
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public RejectUserCommand(long userId) {

    this.userId = userId;
  }

  public long getUserId() {
    return userId;
  }
}