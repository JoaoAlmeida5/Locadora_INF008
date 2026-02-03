package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPricePlugin;
import br.edu.ifba.inf008.shell.Core;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginController implements IPluginController {

    private static PluginController instance;

    public static PluginController getInstance() {
        if (instance == null) {
            instance = new PluginController();
        }
        return instance;
    }

    private List<IPlugin> loadedPlugins = new ArrayList<>();

    @Override

    public boolean init() {
        try {
            loadedPlugins.clear();


            File currentDir = new File("./plugins");
            if (!currentDir.exists()) {
                System.out.println("Pasta './plugins' não encontrada. Tentando '../plugins'...");
                currentDir = new File("../plugins");
            }

            System.out.println("1. Pasta plugins: " + currentDir.getAbsolutePath());

            if (!currentDir.exists()) {
                System.out.println("ERRO: Pasta de plugins não encontrada em lugar nenhum!");
                return false;
            }

            FilenameFilter jarFilter = (dir, name) -> name.toLowerCase().endsWith(".jar");
            String[] pluginFiles = currentDir.list(jarFilter);

            System.out.println("2. Arquivos JAR encontrados: " + (pluginFiles == null ? 0 : pluginFiles.length));

            if (pluginFiles == null || pluginFiles.length == 0) {
                return true;
            }

            URL[] jars = new URL[pluginFiles.length];
            for (int i = 0; i < pluginFiles.length; i++) {
                jars[i] = (new File(currentDir, pluginFiles[i])).toURI().toURL();
            }

            URLClassLoader ulc = new URLClassLoader(jars, App.class.getClassLoader());
            ServiceLoader<IPlugin> sl = ServiceLoader.load(IPlugin.class, ulc);


            System.out.println("3. Iniciando varredura com ServiceLoader...");

            boolean achou = false;
            for (IPlugin plugin : sl) {
                achou = true;

                System.out.println("Plugin classloader: "
                        + plugin.getClass().getClassLoader());
                System.out.println("IPlugin (App) classloader: "
                        + IPlugin.class.getClassLoader());

                System.out.println("   -> ACHEI UM! " + plugin.getClass().getName());
                loadedPlugins.add(plugin);
            }

            if (!achou) {
                System.out.println("4. ALERTA: O JAR foi lido, mas o ServiceLoader NÃO ACHOU nenhum plugin dentro dele.");
                System.out.println("   Verifique se a pasta META-INF/services está correta dentro do projeto do plugin.");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startPlugins() {
        for (IPlugin plugin : loadedPlugins) {
            try {

                plugin.init(Core.getInstance());

            } catch (Exception e) {
                System.out.println("Erro ao inicializar plugin: " + plugin.getClass().getName());
                e.printStackTrace();
            }
        }
    }

    public IPricePlugin getPricePlugin() {
        for (IPlugin plugin : loadedPlugins) {
            if (plugin instanceof IPricePlugin) {
                return (IPricePlugin) plugin;
            }
        }
        return null;
    }


    public br.edu.ifba.inf008.interfaces.IVehiclePlugin getVehiclePlugin(String type) {
        for (IPlugin plugin : loadedPlugins) {
            if (plugin instanceof br.edu.ifba.inf008.interfaces.IVehiclePlugin) {
                br.edu.ifba.inf008.interfaces.IVehiclePlugin vp = (br.edu.ifba.inf008.interfaces.IVehiclePlugin) plugin;
                if (vp.getType().equalsIgnoreCase(type)) {
                    return vp;
                }
            }
        }
        return null;
    }
}