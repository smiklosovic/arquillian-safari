/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.droidium.openblend;

import java.io.File;

import org.arquillian.droidium.container.api.AndroidDevice;
import org.arquillian.droidium.native_.api.Instrumentable;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AeroGearTestCase {

    @Drone
    WebDriver mobile;

    @Deployment(name = "mobile-app")
    @Instrumentable(viaPort = 8081)
    @TargetsContainer("mobile")
    public static JavaArchive getMobileDeployment() {
        return ShrinkWrap.createFromZipFile(JavaArchive.class, new File("android-devconf2014.apk"));
    }

    @FindBy(id = "action_add")
    WebElement addItemButton;

    @FindBy(id = "text")
    WebElement inputField;

    @FindBy(id = "add")
    WebElement itemAdd;

    @Test
    @InSequence(1)
    @OperateOnDeployment("mobile-app")
    public void todolist(@ArquillianResource AndroidDevice device) {
        device.getActivityManagerProvider()
            .getActivityManager()
            .startActivity("com.tadeaskriz.devconf2014.MainActivity_");

        addItemButton.click();

        Graphene.waitGui(mobile).until().element(inputField).is().visible();

        inputField.sendKeys("Automatize tests!");

        itemAdd.click();

    }
}