package com.springwebflux.command;

import com.springwebflux.model.command.SaveCommandRequest;
import com.springwebflux.model.entity.User;

public interface SaveCommand extends Command<SaveCommandRequest, User> {
}
