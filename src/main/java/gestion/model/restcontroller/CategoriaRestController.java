package gestion.model.restcontroller;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestion.model.collections.Categoria;
import gestion.model.service.CategoriaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaRestController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> dameTodas() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> dameUna(@PathVariable ObjectId id) {
        Categoria categoria = categoriaService.findById(id);
        if (categoria == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<?> inserta(@RequestBody Categoria categoria) {
        Categoria guardada = categoriaService.insertOne(categoria);
        if (guardada == null) return ResponseEntity.status(409).body("La categoría ya existe");
        return ResponseEntity.status(201).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edita(@PathVariable ObjectId id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        Categoria actualizada = categoriaService.updateOne(categoria);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borra(@PathVariable ObjectId id) {
        int resultado = categoriaService.deleteOne(id);
        if (resultado == 1) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
