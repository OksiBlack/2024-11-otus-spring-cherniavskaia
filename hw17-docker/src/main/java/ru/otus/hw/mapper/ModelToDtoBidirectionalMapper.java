package ru.otus.hw.mapper;

public interface ModelToDtoBidirectionalMapper <M,T> {
     T mapToDto(M model);

     M mapToModel(T dto);
}
