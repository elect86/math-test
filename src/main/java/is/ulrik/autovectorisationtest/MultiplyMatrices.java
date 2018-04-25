/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package is.ulrik.autovectorisationtest;

import cleargl.GLMatrix;
import com.jogamp.opengl.math.FloatUtil;
import glm_.mat4x4.Mat4;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

@State(Scope.Thread)
@OutputTimeUnit(NANOSECONDS)
@BenchmarkMode(AverageTime)
@Fork(value = 1, jvmArgsAppend = {
        "-XX:-UseSuperWord",
        "-XX:+UnlockDiagnosticVMOptions",
        "-XX:CompileCommand=print,*BenchmarkSIMDBlog.array1"})
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class MultiplyMatrices {

    @State(Scope.Thread)
    public static class Context {
        // input matrices
        public final GLMatrix A = new GLMatrix();
        public final GLMatrix B = new GLMatrix();
        // result matrix
        public GLMatrix C = new GLMatrix();
        public static final GLMatrix zero = new GLMatrix(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});

        public final Mat4 matA = new Mat4();
        public final Mat4 matB = new Mat4();
        public final float[] floats = new float[16];
        public final float[] floats0 = new float[16];
        public final float[] floats1 = new float[16];
        public final Mat4 matC = new Mat4();

        final
        @Setup
        public void setup() {
            C = zero.clone();
            Random r = new Random();
            for (int i = 0; i < 16; i++) {
                A.getFloatArray()[i] = r.nextFloat();
                B.getFloatArray()[i] = r.nextFloat();
            }
            matA.put(A.getFloatArray());
            matB.put(B.getFloatArray());
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++) {
                    floats[i * 4 + j] = matB.get(i, j);
                    floats1[i * 4 + j] = matA.get(i, j);
                }
        }
    }

    @Benchmark
    public void glm(Context context) {
        // 339 ns/op
        // 24,339 ns/op
//        context.matA.times(context.matB, context.matC);
//        context.matA.timesTest(context.matB, context.matC);
//        context.matA.timesTest2(context.matB, context.matC);
//        context.matA.timesTest3(context.matB, context.matC);
        // 22,017 ns/op
        context.matA.times(context.matB, context.matC);
        // 278 ns/op
//        Mat4.Companion.times(context.matA, context.matB,
//                context.floats[0], context.floats[1], context.floats[2], context.floats[3],
//                context.floats[4], context.floats[5], context.floats[6], context.floats[7],
//                context.floats[8], context.floats[9], context.floats[10], context.floats[11],
//                context.floats[12], context.floats[13], context.floats[14], context.floats[15]);
        // 20,080 ns/op
//        float v00 = context.floats[0] * context.floats1[0] + context.floats[4] * context.floats1[1] + context.floats[8] * context.floats1[2] + context.floats[12] * context.floats1[3];
//        float v01 = context.floats[1] * context.floats1[0] + context.floats[5] * context.floats1[1] + context.floats[9] * context.floats1[2] + context.floats[13] * context.floats1[3];
//        float v02 = context.floats[2] * context.floats1[0] + context.floats[6] * context.floats1[1] + context.floats[10] * context.floats1[2] + context.floats[14] * context.floats1[3];
//        float v03 = context.floats[3] * context.floats1[0] + context.floats[7] * context.floats1[1] + context.floats[11] * context.floats1[2] + context.floats[15] * context.floats1[3];
//        float v10 = context.floats[0] * context.floats1[4] + context.floats[4] * context.floats1[5] + context.floats[8] * context.floats1[6] + context.floats[12] * context.floats1[7];
//        float v11 = context.floats[1] * context.floats1[4] + context.floats[5] * context.floats1[5] + context.floats[9] * context.floats1[6] + context.floats[13] * context.floats1[7];
//        float v12 = context.floats[2] * context.floats1[4] + context.floats[6] * context.floats1[5] + context.floats[10] * context.floats1[6] + context.floats[14] * context.floats1[7];
//        float v13 = context.floats[3] * context.floats1[4] + context.floats[7] * context.floats1[5] + context.floats[11] * context.floats1[6] + context.floats[15] * context.floats1[7];
//        float v20 = context.floats[0] * context.floats1[8] + context.floats[4] * context.floats1[9] + context.floats[8] * context.floats1[10] + context.floats[12] * context.floats1[11];
//        float v21 = context.floats[1] * context.floats1[8] + context.floats[5] * context.floats1[9] + context.floats[9] * context.floats1[10] + context.floats[13] * context.floats1[11];
//        float v22 = context.floats[2] * context.floats1[8] + context.floats[6] * context.floats1[9] + context.floats[10] * context.floats1[10] + context.floats[14] * context.floats1[11];
//        float v23 = context.floats[3] * context.floats1[8] + context.floats[7] * context.floats1[9] + context.floats[11] * context.floats1[10] + context.floats[15] * context.floats1[11];
//        float v30 = context.floats[0] * context.floats1[12] + context.floats[4] * context.floats1[13] + context.floats[8] * context.floats1[14] + context.floats[12] * context.floats1[15];
//        float v31 = context.floats[1] * context.floats1[12] + context.floats[5] * context.floats1[13] + context.floats[9] * context.floats1[14] + context.floats[13] * context.floats1[15];
//        float v32 = context.floats[2] * context.floats1[12] + context.floats[6] * context.floats1[13] + context.floats[10] * context.floats1[14] + context.floats[14] * context.floats1[15];
//        float v33 = context.floats[3] * context.floats1[12] + context.floats[7] * context.floats1[13] + context.floats[11] * context.floats1[14] + context.floats[15] * context.floats1[15];
//        context.matC.set(0, 0, v00);
//        context.matC.set(1, 0, v10);
//        context.matC.set(2, 0, v20);
//        context.matC.set(3, 0, v30);
//        context.matC.set(0, 1, v01);
//        context.matC.set(1, 1, v11);
//        context.matC.set(2, 1, v21);
//        context.matC.set(3, 1, v31);
//        context.matC.set(0, 2, v02);
//        context.matC.set(1, 2, v12);
//        context.matC.set(2, 2, v22);
//        context.matC.set(3, 2, v32);
//        context.matC.set(0, 3, v03);
//        context.matC.set(1, 3, v13);
//        context.matC.set(2, 3, v23);
//        context.matC.set(3, 3, v33);
        // 157 ns/op
//        context.floats0[0] = v00;
//        context.floats0[1] = v10;
//        context.floats0[2] = v20;
//        context.floats0[3] = v30;
//        context.floats0[4] = v01;
//        context.floats0[5] = v11;
//        context.floats0[6] = v21;
//        context.floats0[7] = v31;
//        context.floats0[8] = v02;
//        context.floats0[9] = v12;
//        context.floats0[10] = v22;
//        context.floats0[11] = v32;
//        context.floats0[12] = v03;
//        context.floats0[13] = v13;
//        context.floats0[14] = v23;
//        context.floats0[15] = v33;


//        final float a00 = context.floats[0];
//        final float a01 = context.floats[1];
//        final float a02 = context.floats[2];
//        final float a03 = context.floats[3];
//        final float a10 = context.floats[4];
//        final float a11 = context.floats[5];
//        final float a12 = context.floats[6];
//        final float a13 = context.floats[7];
//        final float a20 = context.floats[8];
//        final float a21 = context.floats[9];
//        final float a22 = context.floats[10];
//        final float a23 = context.floats[11];
//        final float a30 = context.floats[12];
//        final float a31 = context.floats[13];
//        final float a32 = context.floats[14];
//        final float a33 = context.floats[15];
//
//        final float b00 = context.floats1[0];
//        final float b01 = context.floats1[1];
//        final float b02 = context.floats1[2];
//        final float b03 = context.floats1[3];
//        final float b10 = context.floats1[4];
//        final float b11 = context.floats1[5];
//        final float b12 = context.floats1[6];
//        final float b13 = context.floats1[7];
//        final float b20 = context.floats1[8];
//        final float b21 = context.floats1[9];
//        final float b22 = context.floats1[10];
//        final float b23 = context.floats1[11];
//        final float b30 = context.floats1[12];
//        final float b31 = context.floats1[13];
//        final float b32 = context.floats1[14];
//        final float b33 = context.floats1[15];
//
//        float v00 = a00 * b00 + a10 * b01 + a20 * b02 + a30 * b03;
//        float v01 = a01 * b00 + a11 * b01 + a21 * b02 + a31 * b03;
//        float v02 = a02 * b00 + a12 * b01 + a22 * b02 + a32 * b03;
//        float v03 = a03 * b00 + a13 * b01 + a23 * b02 + a33 * b03;
//        float v10 = a00 * b10 + a10 * b11 + a20 * b12 + a30 * b13;
//        float v11 = a01 * b10 + a11 * b11 + a21 * b12 + a31 * b13;
//        float v12 = a02 * b10 + a12 * b11 + a22 * b12 + a32 * b13;
//        float v13 = a03 * b10 + a13 * b11 + a23 * b12 + a33 * b13;
//        float v20 = a00 * b20 + a10 * b21 + a20 * b22 + a30 * b23;
//        float v21 = a01 * b20 + a11 * b21 + a21 * b22 + a31 * b23;
//        float v22 = a02 * b20 + a12 * b21 + a22 * b22 + a32 * b23;
//        float v23 = a03 * b20 + a13 * b21 + a23 * b22 + a33 * b23;
//        float v30 = a00 * b30 + a10 * b31 + a20 * b32 + a30 * b33;
//        float v31 = a01 * b30 + a11 * b31 + a21 * b32 + a31 * b33;
//        float v32 = a02 * b30 + a12 * b31 + a22 * b32 + a32 * b33;
//        float v33 = a03 * b30 + a13 * b31 + a23 * b32 + a33 * b33;
//
//        context.floats0[0] = v00;
//        context.floats0[1] = v10;
//        context.floats0[2] = v20;
//        context.floats0[3] = v30;
//        context.floats0[4] = v01;
//        context.floats0[5] = v11;
//        context.floats0[6] = v21;
//        context.floats0[7] = v31;
//        context.floats0[8] = v02;
//        context.floats0[9] = v12;
//        context.floats0[10] = v22;
//        context.floats0[11] = v32;
//        context.floats0[12] = v03;
//        context.floats0[13] = v13;
//        context.floats0[14] = v23;
//        context.floats0[15] = v33;
    }

    @Benchmark
    public void floatUtilGLMatrix(Context context) {
        FloatUtil.multMatrix(context.A.getFloatArray(), context.B.getFloatArray(), context.C.getFloatArray());
    }

    @Benchmark
    public void loop(Context context) {
        float sum;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = 0.0f;

                for (int k = 0; k < 4; k++) {
                    sum += context.A.getFloatArray()[i * 4 + k] * context.B.getFloatArray()[k * 4 + j];
                }

                context.C.getFloatArray()[i * 4 + j] = sum;
            }
        }
    }

    @Benchmark
    public void loopFMA(Context context) {
        float sum;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = 0.0f;
                for (int k = 0; k < 4; k++) {
                    // Math.fma(a,b,c) = a * b + c
                    sum = Math.fma(context.A.getFloatArray()[i * 4 + k], context.B.getFloatArray()[k * 4 + j], sum);
                }
                context.C.getFloatArray()[i * 4 + j] = sum;
            }
        }
    }

    @Benchmark
    public void floatUtilFMA(Context context) {
        multMatrixFMA(context.A.getFloatArray(), context.B.getFloatArray(), context.C.getFloatArray());
    }

    @Benchmark
    public void floatUtilFMALoop(Context context) {
        multMatrixFMALoop(context.A.getFloatArray(), context.B.getFloatArray(), context.C.getFloatArray());
    }

    public static float[] multMatrixFMA(final float[] a, final float[] b, final float[] d) {
        final float b00 = b[0 + 0 * 4];
        final float b10 = b[1 + 0 * 4];
        final float b20 = b[2 + 0 * 4];
        final float b30 = b[3 + 0 * 4];
        final float b01 = b[0 + 1 * 4];
        final float b11 = b[1 + 1 * 4];
        final float b21 = b[2 + 1 * 4];
        final float b31 = b[3 + 1 * 4];
        final float b02 = b[0 + 2 * 4];
        final float b12 = b[1 + 2 * 4];
        final float b22 = b[2 + 2 * 4];
        final float b32 = b[3 + 2 * 4];
        final float b03 = b[0 + 3 * 4];
        final float b13 = b[1 + 3 * 4];
        final float b23 = b[2 + 3 * 4];
        final float b33 = b[3 + 3 * 4];

        float ai0 = a[0 * 4]; // row-0 of a
        float ai1 = a[1 * 4];
        float ai2 = a[2 * 4];
        float ai3 = a[3 * 4];
        d[0 * 4] = Math.fma(ai0, b00, Math.fma(ai1, b10, Math.fma(ai2, b20, ai3 * b30)));
        d[1 * 4] = Math.fma(ai0, b01, Math.fma(ai1, b11, Math.fma(ai2, b21, ai3 * b31)));
        d[2 * 4] = Math.fma(ai0, b02, Math.fma(ai1, b12, Math.fma(ai2, b22, ai3 * b32)));
        d[3 * 4] = Math.fma(ai0, b03, Math.fma(ai1, b13, Math.fma(ai2, b23, ai3 * b33)));

        ai0 = a[1 + 0 * 4]; // row-1 of a
        ai1 = a[1 + 1 * 4];
        ai2 = a[1 + 2 * 4];
        ai3 = a[1 + 3 * 4];
        d[1 + 0 * 4] = Math.fma(ai0, b00, Math.fma(ai1, b10, Math.fma(ai2, b20, ai3 * b30)));
        d[1 + 1 * 4] = Math.fma(ai0, b01, Math.fma(ai1, b11, Math.fma(ai2, b21, ai3 * b31)));
        d[1 + 2 * 4] = Math.fma(ai0, b02, Math.fma(ai1, b12, Math.fma(ai2, b22, ai3 * b32)));
        d[1 + 3 * 4] = Math.fma(ai0, b03, Math.fma(ai1, b13, Math.fma(ai2, b23, ai3 * b33)));

        ai0 = a[2 + 0 * 4]; // row-2 of a
        ai1 = a[2 + 1 * 4];
        ai2 = a[2 + 2 * 4];
        ai3 = a[2 + 3 * 4];
        d[2 + 0 * 4] = Math.fma(ai0, b00, Math.fma(ai1, b10, Math.fma(ai2, b20, ai3 * b30)));
        d[2 + 1 * 4] = Math.fma(ai0, b01, Math.fma(ai1, b11, Math.fma(ai2, b21, ai3 * b31)));
        d[2 + 2 * 4] = Math.fma(ai0, b02, Math.fma(ai1, b12, Math.fma(ai2, b22, ai3 * b32)));
        d[2 + 3 * 4] = Math.fma(ai0, b03, Math.fma(ai1, b13, Math.fma(ai2, b23, ai3 * b33)));

        ai0 = a[3 + 0 * 4]; // row-3 of a
        ai1 = a[3 + 1 * 4];
        ai2 = a[3 + 2 * 4];
        ai3 = a[3 + 3 * 4];
        d[3 + 0 * 4] = Math.fma(ai0, b00, Math.fma(ai1, b10, Math.fma(ai2, b20, ai3 * b30)));
        d[3 + 1 * 4] = Math.fma(ai0, b01, Math.fma(ai1, b11, Math.fma(ai2, b21, ai3 * b31)));
        d[3 + 2 * 4] = Math.fma(ai0, b02, Math.fma(ai1, b12, Math.fma(ai2, b22, ai3 * b32)));
        d[3 + 3 * 4] = Math.fma(ai0, b03, Math.fma(ai1, b13, Math.fma(ai2, b23, ai3 * b33)));

        return d;
    }

    public static float[] multMatrixFMALoop(final float[] a, final float[] b, final float[] d) {
        final float b00 = b[0 + 0 * 4];
        final float b10 = b[1 + 0 * 4];
        final float b20 = b[2 + 0 * 4];
        final float b30 = b[3 + 0 * 4];
        final float b01 = b[0 + 1 * 4];
        final float b11 = b[1 + 1 * 4];
        final float b21 = b[2 + 1 * 4];
        final float b31 = b[3 + 1 * 4];
        final float b02 = b[0 + 2 * 4];
        final float b12 = b[1 + 2 * 4];
        final float b22 = b[2 + 2 * 4];
        final float b32 = b[3 + 2 * 4];
        final float b03 = b[0 + 3 * 4];
        final float b13 = b[1 + 3 * 4];
        final float b23 = b[2 + 3 * 4];
        final float b33 = b[3 + 3 * 4];

        for (int i = 0; i < 4; i++) {
            float ai0 = a[i + 0 * 4]; // row-0 of a
            float ai1 = a[i + 1 * 4];
            float ai2 = a[i + 2 * 4];
            float ai3 = a[i + 3 * 4];
            d[i + 0 * 4] = Math.fma(ai0, b00, Math.fma(ai1, b10, Math.fma(ai2, b20, ai3 * b30)));
            d[i + 1 * 4] = Math.fma(ai0, b01, Math.fma(ai1, b11, Math.fma(ai2, b21, ai3 * b31)));
            d[i + 2 * 4] = Math.fma(ai0, b02, Math.fma(ai1, b12, Math.fma(ai2, b22, ai3 * b32)));
            d[i + 3 * 4] = Math.fma(ai0, b03, Math.fma(ai1, b13, Math.fma(ai2, b23, ai3 * b33)));
        }

        return d;
    }
}
