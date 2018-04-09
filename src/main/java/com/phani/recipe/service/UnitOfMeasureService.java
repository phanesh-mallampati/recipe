package com.phani.recipe.service;

import com.phani.recipe.commands.UnitOfMeasureCommand;
import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();
}
