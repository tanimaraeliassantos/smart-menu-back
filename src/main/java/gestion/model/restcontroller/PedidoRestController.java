package gestion.model.restcontroller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestion.model.collections.Pedido;
import gestion.model.collections.DTO.EstadoUpdateDto;
import gestion.model.service.PedidoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoRestController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<?> dame() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> dameUno(@PathVariable ObjectId id) {
        Pedido pedido = pedidoService.findById(id);
        if (pedido == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/mesa/{mesaId}")
    public ResponseEntity<?> porMesa(@PathVariable String mesaId) {
        return ResponseEntity.ok(pedidoService.findByMesaId(mesaId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> porUsuario(@PathVariable ObjectId usuarioId) {
        return ResponseEntity.ok(pedidoService.findByUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<?> inserta(@RequestBody Pedido pedido) {
        Pedido guardado = pedidoService.insertOne(pedido);
        if (guardado == null) return ResponseEntity.status(409).body("El pedido ya existe");
        return ResponseEntity.status(201).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edita(@PathVariable ObjectId id, @RequestBody Pedido pedido) {
        pedido.setId(id);
        Pedido actualizado = pedidoService.updateOne(pedido);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    // Panel de cocina: PATCH /pedido/{id}/estado  body: { "estado": "PREPARANDO" }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable ObjectId id,
                                           @RequestBody EstadoUpdateDto dto) {
        Pedido actualizado = pedidoService.cambiarEstado(id, dto.getEstado());
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borra(@PathVariable ObjectId id) {
        int resultado = pedidoService.deleteOne(id);
        if (resultado == 1) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}