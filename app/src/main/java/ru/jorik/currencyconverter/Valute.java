package ru.jorik.currencyconverter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by 111 on 12.08.2017.
 */

@Root(name="Valute")
class Valute{
    @Attribute(name="ID", required=false)
    String id;

    @Element(name="NumCode", required=false)
    int numCode;

    @Element(name="CharCode", required=false)
    String charCode;

    @Element(name="Nominal", required=false)
    int nominal;

    @Element(name="Name", required=false)
    String name;

    @Element(name="Value", required=false)
    double value;
}
