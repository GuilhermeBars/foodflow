package br.edu.puccampinas.foodflow.catalog.domain;

import br.edu.puccampinas.foodflow.shared.domain.AggregateRoot;
import br.edu.puccampinas.foodflow.shared.domain.EntityNotFoundException;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Raiz do agregado de catalogo. Um restaurante e dono do seu cardapio: itens so
 * sao criados e alterados atraves dele, o que mantem as regras do cardapio em um
 * unico lugar.
 */
public class Restaurant extends AggregateRoot {

    private final UUID id;
    private String name;
    private String cuisine;
    private boolean active;
    private final List<MenuItem> menu;

    private Restaurant(UUID id, String name, String cuisine, boolean active, List<MenuItem> menu) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = requireText(name, "Nome do restaurante e obrigatorio");
        this.cuisine = requireText(cuisine, "Tipo de cozinha e obrigatorio");
        this.active = active;
        this.menu = new ArrayList<>(menu);
    }

    public static Restaurant create(String name, String cuisine) {
        return new Restaurant(UUID.randomUUID(), name, cuisine, true, List.of());
    }

    public static Restaurant reconstitute(UUID id, String name, String cuisine, boolean active, List<MenuItem> menu) {
        return new Restaurant(id, name, cuisine, active, menu);
    }

    public MenuItem addMenuItem(String name, String description, Money price) {
        MenuItem item = MenuItem.create(name, description, price);
        menu.add(item);
        return item;
    }

    public MenuItem requireItem(UUID itemId) {
        return menu.stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item %s nao encontrado no restaurante %s".formatted(itemId, id)));
    }

    public void deactivate() {
        this.active = false;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String cuisine() {
        return cuisine;
    }

    public boolean isActive() {
        return active;
    }

    public List<MenuItem> menu() {
        return Collections.unmodifiableList(menu);
    }
}
