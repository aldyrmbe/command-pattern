package com.springwebflux.command;

import com.springwebflux.model.command.UpdateCommandRequest;
import com.springwebflux.model.entity.User;

public interface UpdateCommand extends Command<UpdateCommandRequest, User> {
}
