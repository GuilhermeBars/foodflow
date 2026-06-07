/**
 * Shared kernel do FoodFlow: tipos de dominio reutilizados por todos os modulos
 * (valor monetario, eventos de dominio, raiz de agregado e excecoes de negocio).
 *
 * <p>Este pacote nao depende de Spring nem de JPA: e dominio puro, o nucleo mais
 * estavel do sistema, do qual os demais modulos dependem (e nao o contrario).
 */
package br.edu.puccampinas.foodflow.shared.domain;
