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
package io.fabric8.openshift.client.dsl.internal;

import io.fabric8.kubernetes.client.dsl.ClientResource;
import io.fabric8.openshift.api.model.DoneableImageStream;
import io.fabric8.openshift.api.model.ImageStream;
import io.fabric8.openshift.api.model.ImageStreamList;
import io.fabric8.openshift.client.OpenShiftClient;

public class ImageStreamOperationsImpl extends OpenShiftOperation<OpenShiftClient, ImageStream, ImageStreamList, DoneableImageStream,
  ClientResource<ImageStream, DoneableImageStream>> {

  public ImageStreamOperationsImpl(OpenShiftClient client) {
    this(client, client.getNamespace(), null, true, null);
  }

  public ImageStreamOperationsImpl(OpenShiftClient client, String namespace, String name, Boolean cascading, ImageStream item) {
    super(client, "imagestreams", namespace, name, cascading, item);
  }
}
