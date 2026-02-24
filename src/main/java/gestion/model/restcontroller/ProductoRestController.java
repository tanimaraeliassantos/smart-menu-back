package gestion.model.restcontroller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestion.model.collections.Producto;
import gestion.model.service.ProductoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/producto")
@RequiredArgsConstructor
public class ProductoRestController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<?> dameTodos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    // GET /producto/disponibles?restauranteId=abc123
    @GetMapping("/disponibles")
    public ResponseEntity<?> disponibles(@RequestParam ObjectId restauranteId) {
        return ResponseEntity.ok(productoService.findDisponiblesByRestaurante(restauranteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> dameUno(@PathVariable ObjectId id) {
        Producto producto = productoService.findById(id);
        if (producto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<?> inserta(@RequestBody Producto producto) {
        Producto guardado = productoService.insertOne(producto);
        if (guardado == null) return ResponseEntity.status(409).body("El producto ya existe");
        return ResponseEntity.status(201).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifica(@PathVariable ObjectId id, @RequestBody Producto producto) {
        producto.setId(id);
        Producto actualizado = productoService.updateOne(producto);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> elimina(@PathVariable ObjectId id) {
        int resultado = productoService.deleteOne(id);
        if (resultado == 1) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}