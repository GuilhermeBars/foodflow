/**
 * Notificacoes. Observadores independentes reagem aos eventos de dominio do
 * pedido (padrao Observer, via o mecanismo de eventos do Spring). O modulo de
 * pedidos publica os eventos sem conhecer nenhum destes observadores, o que
 * permite adicionar novos canais de notificacao sem toca-lo.
 */
package br.edu.puccampinas.foodflow.notification.application;
