package com.teste.primeiroexemplo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundException;
import com.teste.primeiroexemplo.repository.ProdutoRepository;
import com.teste.primeiroexemplo.shared.ProdutoDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Metodo para retornar todos os produtos
     * 
     * @return Lista de produtos
     */
    public List<ProdutoDTO> obterTodos() {
        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna o produto encontrado pelo seu id
     * 
     * @param id do produto que sera localizado
     * @return Retorna um produto caso seja encontrado
     */
    public Optional<ProdutoDTO> obterPorId(Integer id) {

        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id: " + id + " nao encontrado");
        }

        ProdutoDTO DTO = new ModelMapper().map(produto.get(), ProdutoDTO.class);
        return Optional.of(DTO);
    }

    /**
     * Metodo para adicionar produto na list
     * 
     * @param produto que sera adicionado
     * @return retorna o produto que foi adicionado na list
     */
    public ProdutoDTO adicionar(ProdutoDTO produtoDTO) {

        produtoDTO.setId(null);

        // Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // Converter produtoDTO em produto
        Produto produto = mapper.map(produtoDTO, Produto.class);

        // Salvar o produto no DB
        produto = produtoRepository.save(produto);

        // Atualizar o Id de produtoDTO com o recebido em produto
        produtoDTO.setId(produto.getId());

        return produtoDTO;
    }

    /**
     * Metodo para deletar o produto por id
     * 
     * @param id do produto a ser deletado
     */
    public void deletar(Integer id) {
        // Verificar se produto existe
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Nao foi possivel deletar o produto com id: " + id);
        }

        produtoRepository.deleteById(id);
    }

    /**
     * Metodo para atualizar o produto na lista
     * 
     * @param produto que sera atualizado
     * @return retorna o produto apos atualizar a lista
     */
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDTO) {
        // Passar ID para produtoDTO
        produtoDTO.setId(id);

        // Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // Converter produtoDTO em produto
        Produto produto = mapper.map(produtoDTO, Produto.class);

        // Atualizar o produto no DB
        produtoRepository.save(produto);

        return produtoDTO;
    }

}
