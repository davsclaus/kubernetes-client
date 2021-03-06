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

import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.client.KubernetesClientException;
import com.ning.http.client.AsyncHttpClient;
import io.fabric8.kubernetes.client.internal.URLUtils;
import io.fabric8.openshift.api.model.DoneableTemplate;
import io.fabric8.openshift.api.model.Parameter;
import io.fabric8.openshift.api.model.Template;
import io.fabric8.openshift.api.model.TemplateList;
import io.fabric8.openshift.client.OpenShiftClient;
import io.fabric8.openshift.client.ParameterValue;
import io.fabric8.openshift.client.dsl.ClientTemplateResource;
import io.fabric8.openshift.client.dsl.TemplateOperation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TemplateOperationsImpl
  extends OpenShiftOperation<OpenShiftClient, Template, TemplateList, DoneableTemplate, ClientTemplateResource<Template, KubernetesList, DoneableTemplate>>
  implements TemplateOperation {

  public TemplateOperationsImpl(OpenShiftClient client) {
    this(client, client.getNamespace(), null, true, null);
  }

  public TemplateOperationsImpl(OpenShiftClient client, String namespace, String name, Boolean cascading, Template item) {
    super(client, "templates", namespace, name, cascading, item);
  }

  @Override
  public KubernetesList process(ParameterValue... values) {
    Template t = get();
    AsyncHttpClient.BoundRequestBuilder requestBuilder = null;
    Map<String, String> valuesMap = new HashMap<>(values.length);
    for (ParameterValue pv : values) {
      valuesMap.put(pv.getName(), pv.getValue());
    }
    try {
      for (Parameter p : t.getParameters()) {
        String v = valuesMap.get(p.getName());
        if (v != null) {
          p.setGenerate(null);
          p.setValue(v);
        }
      }

      requestBuilder = getClient().getHttpClient().preparePost(getProcessUrl().toString());

      requestBuilder.setBody(OBJECT_MAPPER.writer().writeValueAsString(t));
      t = handleResponse(requestBuilder, 201);
      KubernetesList l = new KubernetesList();
      l.setItems(t.getObjects());
      return l;
    } catch (Exception e) {
      throw KubernetesClientException.launderThrowable(e);
    }
  }

  private URL getProcessUrl() throws MalformedURLException {
    URL requestUrl = getRootUrl();
    if (getNamespace() != null) {
      requestUrl = new URL(URLUtils.join(requestUrl.toString(), "namespaces" , getNamespace()));
    }
    requestUrl = new URL(URLUtils.join(requestUrl.toString(), "processedtemplates"));
    return requestUrl;
  }
}
