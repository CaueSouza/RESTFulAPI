package com.teste.primeiroexemplo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundException;

import org.springframework.stereotype.Repository;

@Repository
public class ProdutoRepository_old {

    private List<Produto> produtos = new ArrayList<>();
    private Integer ultimoId = 0;

    /**
     * Metodo para retornar todos os produtos
     * 
     * @return Lista de produtos
     */
    public List<Produto> obterTodos() {
        return produtos;
    }

    /**
     * Metodo que retorna o produto encontrado pelo seu id
     * 
     * @param id do produto que sera localizado
     * @return Retorna um produto caso seja encontrado
     */
    public Optional<Produto> obterPorId(Integer id) {
        return produtos
                .stream()
                .filter(produto -> produto.getId() == id)
                .findFirst();
    }

    /**
     * Metodo para adicionar produto na list
     * 
     * @param produto que sera adicionado
     * @return retorna o produto que foi adicionado na list
     */
    public Produto adicionar(Produto produto) {
        ultimoId++;
        produto.setId(ultimoId);
        produtos.add(produto);
        return produto;
    }

    /**
     * Metodo para atualizar o produto na lista
     * 
     * @param produto que sera atualizado
     * @return retorna o produto apos atualizar a lista
     */
    public Produto atualizar(Produto produto) {
        Optional<Produto> produtoEncontrado = obterPorId(produto.getId());

        if (produtoEncontrado.isEmpty()) {
            throw new ResourceNotFoundException("Produto nao encontrado");
        }

        deletar(produto.getId());
        produtos.add(produto);

        return produto;
    }

    /**
     * Metodo para deletar o produto por id
     * 
     * @param id do produto a ser deletado
     */
    public void deletar(Integer id) {
        produtos.removeIf(produto -> produto.getId() == id);
    }
}
