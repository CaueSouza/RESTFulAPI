package com.teste.primeiroexemplo.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.teste.primeiroexemplo.model.exception.ResourceNotFoundException;
import com.teste.primeiroexemplo.services.ProdutoService;
import com.teste.primeiroexemplo.shared.ProdutoDTO;
import com.teste.primeiroexemplo.view.model.ProdutoRequest;
import com.teste.primeiroexemplo.view.model.ProdutoResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> obterTodos() {
        List<ProdutoDTO> produtos = produtoService.obterTodos();

        List<ProdutoResponse> response = produtos.stream()
                .map(produtoDTO -> new ModelMapper().map(produtoDTO, ProdutoResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProdutoResponse>> obterPorId(@PathVariable Integer id) {
        try {
            Optional<ProdutoDTO> produtoDTO = produtoService.obterPorId(id);

            ProdutoResponse response = new ModelMapper().map(produtoDTO.get(), ProdutoResponse.class);

            return new ResponseEntity<>(Optional.of(response), HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> adicionar(@RequestBody ProdutoRequest produtoRequest) {
        ModelMapper mapper = new ModelMapper();

        ProdutoDTO produtoDTO = mapper.map(produtoRequest, ProdutoDTO.class);

        produtoDTO = produtoService.adicionar(produtoDTO);

        ProdutoResponse response = mapper.map(produtoDTO, ProdutoResponse.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Integer id,
            @RequestBody ProdutoRequest produtoRequest) {
        ModelMapper mapper = new ModelMapper();
        ProdutoDTO produtoDTO = mapper.map(produtoRequest, ProdutoDTO.class);
        produtoDTO = produtoService.atualizar(id, produtoDTO);

        ProdutoResponse response = mapper.map(produtoDTO, ProdutoResponse.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
