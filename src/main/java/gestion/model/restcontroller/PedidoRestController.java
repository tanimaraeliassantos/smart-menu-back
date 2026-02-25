package gestion.model.restcontroller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gestion.model.collections.DTO.EstadoUpdateDto;
import gestion.model.collections.Pedido;
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
    public ResponseEntity<?> dameUno(@PathVariable String id) {
        if (!ObjectId.isValid(id)) return ResponseEntity.badRequest().body("ID inválido");
        Pedido pedido = pedidoService.findById(new ObjectId(id));
        if (pedido == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/mesa/{mesaId}")
    public ResponseEntity<?> porMesa(@PathVariable String mesaId) {
        return ResponseEntity.ok(pedidoService.findByMesaId(mesaId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> porUsuario(@PathVariable String usuarioId) {
        if (!ObjectId.isValid(usuarioId)) return ResponseEntity.badRequest().body("ID inválido");
        return ResponseEntity.ok(pedidoService.findByUsuarioId(new ObjectId(usuarioId)));
    }

    @PostMapping
    public ResponseEntity<?> inserta(@RequestBody Pedido pedido) {
        pedido.setId(null); // dejamos que MongoDB genere el ID
        Pedido guardado = pedidoService.insertOne(pedido);
        if (guardado == null) return ResponseEntity.status(409).body("El pedido ya existe");
        return ResponseEntity.status(201).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edita(@PathVariable String id, @RequestBody Pedido pedido) {
        if (!ObjectId.isValid(id)) return ResponseEntity.badRequest().body("ID inválido");
        pedido.setId(new ObjectId(id));
        Pedido actualizado = pedidoService.updateOne(pedido);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable String id,
                                           @RequestBody EstadoUpdateDto dto) {
        if (!ObjectId.isValid(id)) return ResponseEntity.badRequest().body("ID inválido");
        Pedido actualizado = pedidoService.cambiarEstado(new ObjectId(id), dto.getEstado());
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borra(@PathVariable String id) {
        if (!ObjectId.isValid(id)) return ResponseEntity.badRequest().body("ID inválido");
        int resultado = pedidoService.deleteOne(new ObjectId(id));
        if (resultado == 1) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}