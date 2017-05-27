/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.demo.dynamicmenu;

import java.text.DecimalFormat;
import java.util.Optional;

import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.dynamicmenu.backend.DataService;
import com.vaadin.flow.demo.dynamicmenu.data.Product;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.html.HtmlContainer;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;

/**
 * A view which shows a product.
 *
 * @since
 * @author Vaadin Ltd
 */
@Tag("p")
public class ProductView extends HtmlContainer implements View {

    private Optional<Product> currentProduct;

    @Override
    public void onLocationChange(LocationChangeEvent locationChangeEvent) {
        int productId = getProductId(locationChangeEvent);
        currentProduct = DataService.get().getProductById(productId);
        if (!currentProduct.isPresent()) {
            setText("Product does not exist or has been removed");
            return;
        }

        Product product = currentProduct.get();
        getElement().removeAllChildren();
        getElement().appendChild(ElementFactory
                .createStrong("Information about product " + product.getId()));
        getElement().appendChild(
                ElementFactory.createDiv("Title: " + product.getProductName()));
        getElement()
                .appendChild(ElementFactory.createDiv("Price: " + DecimalFormat
                        .getCurrencyInstance().format(product.getPrice())));
        getElement().appendChild(ElementFactory
                .createDiv("Stock count: " + product.getStockCount()));

    }

    /**
     * Gets the product id based on the URL.
     *
     * @param locationChangeEvent
     *            a location change event used for finding the URL and
     *            parameters
     * @return the product id or -1 if the URL does not refer to a product view
     */
    public static int getProductId(LocationChangeEvent locationChangeEvent) {
        if (!"product"
                .equals(locationChangeEvent.getLocation().getFirstSegment())) {
            return -1;
        }
        try {
            return Integer.parseInt(locationChangeEvent.getPathParameter("id"));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String getTitle(LocationChangeEvent locationChangeEvent) {
        if (!currentProduct.isPresent()) {
            return "Unknown product";
        } else {
            return "Product: " + currentProduct.get().getProductName();
        }
    }

}