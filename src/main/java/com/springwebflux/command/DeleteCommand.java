package com.springwebflux.command;

import com.springwebflux.model.command.DeleteRequest;
import com.springwebflux.model.entity.User;

public interface DeleteCommand extends Command<DeleteRequest, User> {
}
