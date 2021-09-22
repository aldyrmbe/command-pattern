package com.springwebflux.command;

import com.springwebflux.model.command.FindByNameCommandRequest;
import com.springwebflux.model.entity.User;

public interface FindByNameCommand extends Command<FindByNameCommandRequest, User> {

}
