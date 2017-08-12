package ru.jorik.currencyconverter;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by 111 on 12.08.2017.
 */
@Root
public class ValCurs {

    @ElementList(inline=true)
    List<Valute> valuteList;

}
