package com.javafx.experiments.scenicview.utils;

import java.rmi.RemoteException;

import javafx.stage.Stage;

import com.javafx.experiments.scenicview.ScenicView;
import com.javafx.experiments.scenicview.connector.remote.RemoteConnectorImpl;
import com.javafx.experiments.scenicview.update.RemoteVMsUpdateStrategy;
import com.sun.javafx.application.PlatformImpl;

public class RemoteScenicViewLauncher {

    private static ScenicView view;

    private RemoteScenicViewLauncher() {
    }

    public static void start() {
        final RemoteVMsUpdateStrategy strategy = new RemoteVMsUpdateStrategy();
        PlatformImpl.startup(new Runnable() {

            @Override public void run() {
                RemoteConnectorImpl.debug("Platform running");
                final Stage stage = new Stage();
                // workaround for RT-10714
                stage.setWidth(640);
                stage.setHeight(800);
                stage.setTitle("Scenic View v" + ScenicView.VERSION);
                view = new ScenicView(strategy, stage);
                ScenicView.show(view, stage);
            }
        });
        RemoteConnectorImpl.debug("Startup done");
        while (view == null) {
            try {
                Thread.sleep(500);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

        RemoteConnectorImpl.debug("Creating server");
        try {
            strategy.setFXConnector(new RemoteConnectorImpl());
        } catch (final RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        RemoteConnectorImpl.debug("Server done");
    }
}