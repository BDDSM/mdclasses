package com.github._1c_syntax.mdclasses;

import com.github._1c_syntax.mdclasses.metadata.additional.*;
import com.github._1c_syntax.mdclasses.metadata.configurations.AbstractConfiguration;
import com.github._1c_syntax.mdclasses.metadata.ConfigurationBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationOriginTest {

  private final String PATH_TO_SUPPORT = "src/test/resources/support/original";

  @Test
  void testBuilder() {

    File srcPath = new File("src/test/resources/metadata/original");
    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(srcPath.toPath());
    AbstractConfiguration configuration = configurationBuilder.build();

    assertThat(configuration).isNotNull();

    assertThat(configuration.getConfigurationSource()).isEqualTo(ConfigurationSource.DESIGNER);
    assertThat(configuration.getScriptVariant() == ScriptVariant.RUSSIAN).isTrue();
    assertThat(CompatibilityMode.compareTo(configuration.getCompatibilityMode(), new CompatibilityMode(3, 13))).isEqualTo(0);
    assertThat(configuration.getModulesByType().size() > 0).isTrue();

    File file = new File("src/test/resources/metadata/original/Documents/ПоступлениеТоваровУслуг/Ext/ManagerModule.bsl");
    assertThat(configuration.getModuleType(file.toURI())).isEqualTo(ModuleType.ManagerModule);

    file = new File("src/test/resources/metadata/original/CommonModules/ПростойОбщийМодуль/Ext/Module.bsl");
    assertThat(configuration.getModuleType(file.toURI())).isEqualTo(ModuleType.CommonModule);

  }

  @Test
  void testErrorBuild() {

    Path srcPath = Paths.get("src/test/resources/metadata");
    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(srcPath);
    AbstractConfiguration configuration = configurationBuilder.build();

    assertThat(configuration).isNotNull();
  }

  @Test
  void testConfigurationSupport() {

    Path srcPath = Paths.get(PATH_TO_SUPPORT);
    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(srcPath);
    AbstractConfiguration configuration = configurationBuilder.build();

    // пока просто проверим что там чтото есть
    assertThat(configuration.getModulesBySupport().size()).isNotZero();

    Path path1 = Paths.get(PATH_TO_SUPPORT, "Catalogs/ПервыйСправочник/Ext/ObjectModule.bsl").toAbsolutePath();
    assertThat(configuration.getModuleSupport(path1.toUri())).isEqualTo(SupportVariant.NOT_EDITABLE);

    Path path2 = Paths.get(PATH_TO_SUPPORT, "Ext/SessionModule.bsl").toAbsolutePath();
    assertThat(configuration.getModuleSupport(path2.toUri())).isEqualTo(SupportVariant.SAVED);

    Path path3 = Paths.get(PATH_TO_SUPPORT, "Documents/ПервыйДокумент/Ext/ObjectModule.bsl").toAbsolutePath();
    assertThat(configuration.getModuleSupport(path3.toUri())).isEqualTo(SupportVariant.OFF);

  }

}
