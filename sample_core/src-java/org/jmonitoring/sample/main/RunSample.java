package org.jmonitoring.sample.main;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/


/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class RunSample
{
    private static final int SLEEP_TEST_TIME = 5000;
    private static final int NB3 = 32;
    private static final int NB2 = 31;
    private static final int NB1 = 30;
    /**
     * For the Sample
     * @param pArgs For the Sample
     */
    public static void main(String[] pArgs)
    {
        new RunSample().run();
        // On attend pour être sur de l'insertion
        try
        {
            //Empirique
            Thread.sleep(SLEEP_TEST_TIME);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * For the Sample
     *
     */
    public void run()
    {

        Inventory inventory = new Inventory();

        Item item1 = new Item("1", NB1);

        Item item2 = new Item("2", NB2);

        Item item3 = new Item("3", NB3);

        inventory.addItem(item1);

        inventory.addItem(item2);

        inventory.addItem(item3);
        ShoppingCart sc = new ShoppingCart();

        ShoppingCartOperator.addShoppingCartItem(sc, inventory, item1);

        ShoppingCartOperator.addShoppingCartItem(sc, inventory, item2);

        try
        {
            ShoppingCartOperator.addShoppingCartItem(sc, inventory, item3);
        } catch (RuntimeException e)
        {
            // C'est juste pour tester la remontée d'exception
            System.out.print("");
        }

    }

}
