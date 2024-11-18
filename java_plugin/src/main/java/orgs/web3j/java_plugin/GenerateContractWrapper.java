/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package orgs.web3j.java_plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gradle.api.provider.Property;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;

import org.web3j.codegen.SolidityFunctionWrapperGenerator;

public abstract class GenerateContractWrapper
        implements WorkAction<GenerateContractWrapper.Parameters> {

    @Override
    public void execute() {
        final String typesFlag =
                getParameters().getUseNativeJavaTypes().get() ? "--javaTypes" : "--solidityTypes";

        final String generateBoth = getParameters().getGenerateBoth().get() ? "--generateBoth" : "";

        List<String> arguments =
                new ArrayList<>(
                        Arrays.asList(
                                "--abiFile",
                                getParameters().getContractAbi().get().getAbsolutePath(),
                                "--binFile",
                                getParameters().getContractBin().get().getAbsolutePath(),
                                "--outputDir",
                                getParameters().getOutputDir().get(),
                                "--package",
                                getParameters().getPackageName().get(),
                                "--contractName",
                                getParameters().getContractName().get(),
                                "--addressLength",
                                String.valueOf(getParameters().getAddressLength().get()),
                                typesFlag));

        if (!generateBoth.isEmpty()) {
            arguments.add(generateBoth);
        }

        SolidityFunctionWrapperGenerator.main(arguments.toArray(new String[0]));
    }

    public interface Parameters extends WorkParameters {

        Property<String> getContractName();

        Property<File> getContractBin();

        Property<File> getContractAbi();

        Property<String> getOutputDir();

        Property<String> getPackageName();

        Property<Integer> getAddressLength();

        Property<Boolean> getUseNativeJavaTypes();

        Property<Boolean> getGenerateBoth();
    }
}
