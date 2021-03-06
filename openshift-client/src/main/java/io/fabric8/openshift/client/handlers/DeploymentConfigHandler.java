/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.openshift.client.handlers;

import io.fabric8.kubernetes.client.Client;
import io.fabric8.kubernetes.client.ResourceHandler;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.client.OpenShiftClient;
import io.fabric8.openshift.client.dsl.internal.DeploymentConfigOperationsImpl;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component
@Service
public class DeploymentConfigHandler implements ResourceHandler<DeploymentConfig> {
  @Override
  public String getKind() {
    return DeploymentConfig.class.getSimpleName();
  }

  @Override
  public DeploymentConfig create(Client client, String namespace, DeploymentConfig item) {
    try (OpenShiftClient osClient = client.adapt(OpenShiftClient.class)) {
      return new DeploymentConfigOperationsImpl(osClient, namespace, null, true, item).create();
    }
  }

  @Override
  public Boolean delete(Client client, String namespace, DeploymentConfig item) {
    try (OpenShiftClient osClient = client.adapt(OpenShiftClient.class)) {
      return new DeploymentConfigOperationsImpl(osClient, namespace, null, true, item).delete(item);
    }
  }
}
