package com.formacionbdi.springboot.app.item.springbootservicioitem.controllers;

import com.formacionbdi.springboot.app.item.springbootservicioitem.models.Item;
import com.formacionbdi.springboot.app.item.springbootservicioitem.models.Producto;
import com.formacionbdi.springboot.app.item.springbootservicioitem.models.service.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private CircuitBreakerFactory cbFactory;
    @Autowired
    @Qualifier("serviceFeign")
    private ItemService itemService;

    @GetMapping("/listar")
    public List<Item> listar(@RequestParam(name = "nombre", required = false) String nombre, @RequestHeader(name = "token-request", required = false) String token){
        log.info(nombre);
        log.info(token);
        return itemService.findAll();
    }

    @GetMapping("/ver/{id}/cantidad/{cantidad}")
    public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad){
        return cbFactory.create("items")
                .run(() -> itemService.findById(id, cantidad), e -> metodoAlternativo(id, cantidad, e));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
    @GetMapping("/ver2/{id}/cantidad/{cantidad}")
    public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad){
        return itemService.findById(id, cantidad);
    }

    public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
        log.error("error: {}", e.getMessage());
        Item item = new Item();
        Producto producto = new Producto();

        item.setCantidad(cantidad);
        producto.setId(id);
        producto.setNombre("Camara Sony - Manual");
        producto.setPrecio(500.00);
        item.setProducto(producto);
        return item;
    }
}
