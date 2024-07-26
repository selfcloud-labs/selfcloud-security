package pl.selfcloud.security.saga.user.create.proxy;

import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

import pl.selfcloud.customer.api.saga.command.CreateCustomerCommand;
import pl.selfcloud.customer.api.saga.command.DeleteCustomerCommand;
import pl.selfcloud.customer.api.saga.reply.CustomerCreatedReply;
import pl.selfcloud.customer.api.saga.reply.CustomerDeletedReply;

public class CustomerServiceProxy {
  public CommandEndpoint<CreateCustomerCommand> createCustomer = CommandEndpointBuilder
      .forCommand(CreateCustomerCommand.class)
      .withChannel("customerService")
      .withReply(CustomerCreatedReply.class)
      .build();

  public CommandEndpoint<DeleteCustomerCommand> deleteCustomer = CommandEndpointBuilder
      .forCommand(DeleteCustomerCommand.class)
      .withChannel("customerService")
      .withReply(CustomerDeletedReply.class)
      .build();
}
