package com.formacionbdi.springboot.app.item.springbootservicioitem.models.service;

import com.formacionbdi.springboot.app.item.springbootservicioitem.models.Item;

import java.util.List;

public interface ItemService {
    public List<Item> findAll();
    public Item findById(Long id, Integer cantidad);
}
