/*
 * Copyright (c) 2019 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at:
 *
 *     https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.jkube.kit.resource.helm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fabric8.openshift.api.model.Template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jkube.kit.common.Maintainer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration for a helm chart
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class HelmConfig {
  private String apiVersion;
  private String chart;
  private String chartExtension;
  private String version;
  private String description;
  private String home;
  private List<String> sources;
  private List<Maintainer> maintainers;
  private String icon;
  private String appVersion;
  private List<String> keywords;
  private String engine;
  private List<File> additionalFiles;
  /**
   * OpenShift Template YAML files containing the parameters to interpolate in the Chart templates, and generate
   * the values.yaml file.
   */
  private List<Template> parameterTemplates;
  private List<HelmParameter> parameters;
  private List<HelmType> types;
  private String sourceDir;
  private String outputDir;
  private String tarballOutputDir;
  private String tarFileClassifier;
  @Builder.Default
  private List<GeneratedChartListener> generatedChartListeners = new ArrayList<>();
  private HelmRepository stableRepository;
  private HelmRepository snapshotRepository;
  private String security;
  private boolean lintStrict;
  private boolean lintQuiet;
  private boolean debug;
  private boolean dependencyVerify;
  private boolean dependencySkipRefresh;
  private String releaseName;
  private boolean installDependencyUpdate;
  private boolean installWaitReady;
  private boolean disableOpenAPIValidation;
  /**
   * Timeout in seconds
   */
  private int testTimeout;


  @JsonProperty("dependencies")
  private List<HelmDependency> dependencies;

  // Plexus deserialization specific setters
  /**
   * Used by Plexus/Eclipse Sisu deserialization (pom.xml Unmarshalling)
   *
   * @param types String with a comma separated list of {@link HelmType}
   */
  public void setType(String types) {
    setTypes(HelmType.parseString(types));
  }

  @Getter
  public enum HelmType {
    KUBERNETES("helm", "kubernetes", "kubernetes", "Kubernetes"),
    OPENSHIFT("helmshift", "openshift","openshift", "OpenShift");

    private final String classifier;
    private final String sourceDir;
    private final String outputDir;
    private final String description;

    HelmType(String classifier, String sourceDir, String outputDir, String description) {
      this.classifier = classifier;
      this.sourceDir = sourceDir;
      this.outputDir = outputDir;
      this.description = description;
    }

    public static List<HelmType> parseString(String types) {
      return Optional.ofNullable(types)
          .map(t -> t.split(","))
          .map(Stream::of)
          .map(s -> s.filter(StringUtils::isNotBlank).map(String::toUpperCase).map(HelmType::valueOf)
              .collect(Collectors.toList()))
          .orElse(Collections.emptyList());
    }
  }
}
