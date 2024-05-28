public class Algorithm {

    public Result calculate(double a, double b, double e, int m, int f) {
        if (m == 1) {
            return halves(a, b, e, f);
        } else if (m == 2) {
            return newton(a, b, e, f);
        } else return simple(a, b, e, f);
    }

    private double func1(double xx) {
        return 2.3 * Math.pow(xx, 3) + 5.75 * Math.pow(xx, 2) - 7.41 * xx - 10.6;
    }

    private double func2(double xx) {
        return Math.sin(xx) + 0.25 * xx - 1;
    }

    private double func3(double xx) {
        return 1.8 * Math.pow(xx, 3) - Math.pow(xx, 2) - 5 * xx + 1.539;
    }

    private double func1der(double xx) {
        return 6.9 * Math.pow(xx, 2) + 11.5 * xx - 7.41;
    }

    private double func2der(double xx) {
        return Math.cos(xx) + 0.25;
    }

    private double func3der(double xx) {
        return 5.4 * Math.pow(xx, 2) - 2 * xx - 5;
    }

    private double func1iter(double xx) {
        return (2.3 * Math.pow(xx, 3) + 5.75 * Math.pow(xx, 2) - 10.6) / 7.41;
    }

    private double func2iter(double xx) {
        return (1 - Math.sin(xx)) / 0.25;
    }

    private double func3iter(double xx) {
        return (1.8 * Math.pow(xx, 3) - Math.pow(xx, 2) + 1.539) / 5;
    }

    private double funciter(double xx, int f) {
        if (f == 1) return func1iter(xx);
        else if (f == 2) return func2iter(xx);
        else return func3iter(xx);
    }

    private double funcder(double xx, int f) {
        if (f == 1) return func1der(xx);
        else if (f == 2) return func2der(xx);
        else return func3der(xx);
    }

    private double func(double xx, int f) {
        if (f == 1) return func1(xx);
        else if (f == 2) return func2(xx);
        else return func3(xx);
    }


    /////////////////////////
    //TODO
    //
    ////////////////////////
    private Result halves(double a, double b, double e, int f) {
        int n = 0;
        double x;
        while (Math.abs(a - b) > e) {
            x = (a + b) / 2;
            if (func(a, f) * func(x, f) > 0) {
                a = x;
            } else {
                b = x;
            }
            n++;
        }
        x = (a + b) / 2;
        return new Result(x, func(x, f), n);
    }

    private Result newton(double a, double b, double e, int f) {
        int n = 0;
        double prev = (a + b) / 2;
        double x = prev - (func(prev, f) / funcder(prev, f));
        while (Math.abs(x - prev) > e) {
            prev = x;
            x = prev - (func(prev, f) / funcder(prev, f));
            n++;
        }
        return new Result(x, func(x, f), n);
    }

    private Result simple(double a, double b, double e, int f) {
        int n = 0;
        double prev = (a + b) / 2;
        double x = funciter(prev, f);
        while (Math.abs(x - prev) > e) {
            prev = x;
            x = funciter(prev, f);
            n++;
        }
        return new Result(x, func(x, f), n);
    }
    ///////////////
    //TODO
    //
    //////////////

    public void verifySingleRoot(int f, double a, double b) throws SameSignsException, NotMonotonicException {
        double fa = func(a, f);
        double fb = func(b, f);

        if (fa * fb > 0) {
            System.out.println("error same signs");
            throw new SameSignsException();
        }

        boolean isIncreasing = true;
        boolean isDecreasing = true;
        for (double x = a; x <= b; x += (b - a) / 100.0) {
            double d = funcder(x, f);
            if (d > 0) {
                isDecreasing = false;
            } else if (d < 0) {
                isIncreasing = false;
            }
        }

        if (!isIncreasing && !isDecreasing) {
            System.out.println("error not monotonic");
            throw new NotMonotonicException();
        }

        System.out.println("The function has exactly one root between " + a + " and " + b);
    }

    public Result newtonSystem(double x, double y, double e, int s) {
        double dx, dy;
        int iter = 1;
        double[] vec = solve(getA(x, y, s), getB(x, y, s));

        dx = vec[0];
        dy = vec[1];
        x = x + dx;
        y = y + dy;


        while (Math.abs(dx) > e || Math.abs(dy) > e) {
            vec = solve(getA(x, y, s), getB(x, y, s));

            dx = vec[0];
            dy = vec[1];
            x = x + dx;
            y = y + dy;

            iter++;
        }

        return new Result(x, y, iter);
    }

    private double[][] getA(double x, double y, int s) {
        if (s == 1) {
            return s1a(x, y);
        } else return s2a(x, y);
    }

    private double[] getB(double x, double y, int s) {
        if (s == 1) {
            return s1b(x, y);
        } else return s2b(x, y);
    }

    private double[][] s1a(double x, double y) {
        return new double[][]{
                {2 * x, 2 * y},
                {-6 * x, 1}
        };
    }

    private double[] s1b(double x, double y) {
        return new double[]{4 - x * x - y * y, 3 * x * x - y};
    }

    private double[][] s2a(double x, double y) {
        return new double[][]{
                {2 * x, 2},
                {3, 2 * y}
        };
    }

    private double[] s2b(double x, double y) {
        return new double[]{3 - 2 * y - x * x, 5 - 3 * x - y * y};
    }

    private static double[] gaussSeidel(int n, double[][] a, double[] b, double[] x, double e, int m) {
        int k = 1;
        while (k <= m) {
            double sig = 0;

            for (int i = 0; i < n; i++) {
                double s = 0;
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        s += a[i][j] * x[j];
                    }
                }


                double xx = (b[i] - s) / a[i][i];
                double d = Math.abs(xx - x[i]);

                if (d > sig) {
                    sig = d;
                }
                x[i] = xx;
            }

            if (sig < e) break;

            k += 1;

        }

        boolean nan = false;
        for (int i = 0; i < n; i++) {
            if (Double.isNaN(x[i])) {
                nan = true;
                break;
            }
        }

        if (k > m || nan) {
            return null;
        } else {
            return x;
        }
    }

    public static double[] solve(double[][] a, double[] b) {
        int n = b.length;

        for (int pivot = 0; pivot < n - 1; pivot++) {
            for (int row = pivot + 1; row < n; row++) {
                double factor = a[row][pivot] / a[pivot][pivot];
                for (int col = pivot; col < n; col++) {
                    a[row][col] -= factor * a[pivot][col];
                }
                b[row] -= factor * b[pivot];
            }
        }

        double[] res = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += a[i][j] * res[j];
            }
            res[i] = (b[i] - sum) / a[i][i];
        }

        return res;
    }

}
