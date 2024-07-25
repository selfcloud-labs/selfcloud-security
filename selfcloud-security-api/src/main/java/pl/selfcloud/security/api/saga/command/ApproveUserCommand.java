package pl.selfcloud.security.api.saga.command;

import io.eventuate.tram.commands.common.Command;

public class ApproveUserCommand implements Command {
  private long userId;

  private ApproveUserCommand() {
  }


  public ApproveUserCommand(long userId) {

    this.userId = userId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }
}