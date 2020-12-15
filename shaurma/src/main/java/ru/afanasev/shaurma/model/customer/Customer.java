package ru.afanasev.shaurma.model.customer;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.afanasev.shaurma.model.promocode.Promocode;

@Getter
@Setter
@Builder
public class Customer {
    private Long id;
    private String name;
    private Integer number;
    @Setter(AccessLevel.NONE)
    private final List<Promocode> listPromocodes = new ArrayList<>();

    public void addPromocode(Promocode promocode){
        listPromocodes.add(promocode);
    }
    public void removePromocode(Promocode promocode){
        listPromocodes.remove(promocode);
    }
}