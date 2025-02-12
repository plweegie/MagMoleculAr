/*
 * Copyright 2018 Google LLC All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications (C) 2020 Jan K Szymanski
 */
package com.google.ar.sceneform.ux;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

/**
 * Manipulates the rotation of a {@link BaseTransformableNode} using a {@link
 * TwistGestureRecognizer}.
 */
public class RotationController extends BaseTransformationController<TwistGesture> {

  // Rate that the node rotates in degrees per degree of twisting.
  private float rotationRateDegrees = 1.0f;

  public RotationController(
      BaseTransformableNode transformableNode, TwistGestureRecognizer gestureRecognizer) {
    super(transformableNode, gestureRecognizer);
  }

  public void setRotationRateDegrees(float rotationRateDegrees) {
    this.rotationRateDegrees = rotationRateDegrees;
  }

  public float getRotationRateDegrees() {
    return rotationRateDegrees;
  }

  @Override
  public boolean canStartTransformation(TwistGesture gesture) {
    return true;
  }

  @Override
  public void onContinueTransformation(TwistGesture gesture) {
    float rotationAmount = -gesture.getDeltaRotationDegrees() * rotationRateDegrees;
    Quaternion rotationDelta = new Quaternion(Vector3.forward(), rotationAmount);
    Quaternion localrotation = getTransformableNode().getLocalRotation();
    localrotation = Quaternion.multiply(localrotation, rotationDelta);
    getTransformableNode().setLocalRotation(localrotation);
  }

  @Override
  public void onEndTransformation(TwistGesture gesture) {}
}
