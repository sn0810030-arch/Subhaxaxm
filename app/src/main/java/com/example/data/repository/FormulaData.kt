package com.example.data.repository

import com.example.data.model.FormulaItem

object FormulaData {

    fun getDefaultFormulas(): List<FormulaItem> {
        return listOf(
            // Mathematics
            FormulaItem(
                id = "f_math_1",
                subject = "Mathematics",
                topic = "Quadratic Equations",
                title = "Quadratic Formula (Shreedhar Acharya)",
                formulaLatexOrText = "x = (-b ± √(b² - 4ac)) / (2a)",
                description = "Gives the roots of ax² + bx + c = 0 where a ≠ 0 and Discriminant D = b² - 4ac.",
                units = "Dimensionless",
                workedExample = "For 2x² - 7x + 3 = 0: a=2, b=-7, c=3. D = 49 - 24 = 25. x = (7 ± 5)/4 => x = 3 or x = 1/2."
            ),
            FormulaItem(
                id = "f_math_2",
                subject = "Mathematics",
                topic = "Arithmetic Progressions",
                title = "nth Term and Sum of AP",
                formulaLatexOrText = "aₙ = a + (n - 1)d\nSₙ = (n / 2) [2a + (n - 1)d] = (n / 2) [a + l]",
                description = "Where 'a' is first term, 'd' is common difference, 'n' is number of terms, and 'l' is the last term.",
                units = "Standard units of terms",
                workedExample = "For AP 3, 7, 11... with a=3, d=4: a₁₀ = 3 + 9(4) = 39. S₁₀ = 5(6 + 36) = 210."
            ),
            FormulaItem(
                id = "f_math_3",
                subject = "Mathematics",
                topic = "Trigonometry",
                title = "Fundamental Trigonometric Identities",
                formulaLatexOrText = "1) sin²θ + cos²θ = 1\n2) 1 + tan²θ = sec²θ\n3) 1 + cot²θ = cosec²θ",
                description = "Pythagorean trigonometric identities valid for all angles 0° ≤ θ ≤ 90°.",
                units = "Angles in degrees or radians",
                workedExample = "If sinθ = 3/5, then cosθ = √(1 - 9/25) = 4/5. tanθ = (3/5)/(4/5) = 3/4."
            ),
            FormulaItem(
                id = "f_math_4",
                subject = "Mathematics",
                topic = "Coordinate Geometry",
                title = "Distance & Section Formula",
                formulaLatexOrText = "Distance: d = √[(x₂ - x₁)² + (y₂ - y₁)²]\nSection: P(x, y) = [(m₁x₂ + m₂x₁)/(m₁+m₂), (m₁y₂ + m₂y₁)/(m₁+m₂)]",
                description = "Finds geometric separation and internal point dividing line segment in ratio m₁:m₂.",
                units = "Cartesian units",
                workedExample = "Distance between (0,0) and (3,4) = √(9 + 16) = √25 = 5 units."
            ),
            FormulaItem(
                id = "f_math_5",
                subject = "Mathematics",
                topic = "Surface Areas and Volumes",
                title = "Sphere, Cone & Cylinder Volumes",
                formulaLatexOrText = "Cylinder V = πr²h\nCone V = (1/3)πr²h\nSphere V = (4/3)πr³\nHemisphere V = (2/3)πr³",
                description = "Mensuration formulas for 3-dimensional solids where r is radius and h is height.",
                units = "Cubic units (m³, cm³)",
                workedExample = "Cylinder with r=7cm, h=10cm: V = (22/7) × 49 × 10 = 1540 cm³."
            ),
            // Science - Physics
            FormulaItem(
                id = "f_sci_1",
                subject = "Science",
                topic = "Electricity",
                title = "Ohm's Law & Electric Power",
                formulaLatexOrText = "V = I × R\nP = V × I = I²R = V² / R\nJoule's Heating: H = I²Rt",
                description = "Relates electric potential difference (V), current (I), resistance (R), and dissipated electrical power.",
                units = "V (Volts), I (Amperes), R (Ohms Ω), P (Watts W), H (Joules J)",
                workedExample = "A 220V appliance draws 5A: R = 220/5 = 44 Ω. Power P = 220 × 5 = 1100 W = 1.1 kW."
            ),
            FormulaItem(
                id = "f_sci_2",
                subject = "Science",
                topic = "Light - Reflection & Refraction",
                title = "Mirror & Lens Formulas",
                formulaLatexOrText = "Mirror Formula: 1/f = 1/v + 1/u,  m = -v/u = h'/h\nLens Formula: 1/f = 1/v - 1/u,  m = +v/u = h'/h\nPower: P = 1 / f(in meters)",
                description = "Sign convention: u is always negative. Converging (concave mirror/convex lens) f is positive/negative accordingly.",
                units = "f, v, u in meters or cm; Power in Dioptres (D)",
                workedExample = "Convex lens with f = +20 cm, u = -30 cm: 1/v = 1/20 + (-1/30) = 1/60 => v = +60 cm (real, inverted). Power P = 1/0.2 = +5 D."
            ),
            FormulaItem(
                id = "f_sci_3",
                subject = "Science",
                topic = "Light - Refraction",
                title = "Snell's Law of Refraction",
                formulaLatexOrText = "n₂₁ = sin(i) / sin(r) = v₁ / v₂ = c / v",
                description = "Refractive index of second medium with respect to first medium, where i is angle of incidence and r is angle of refraction.",
                units = "Dimensionless ratio",
                workedExample = "Refractive index of water = 1.33, speed of light c = 3 × 10⁸ m/s => v_water = (3 × 10⁸)/1.33 ≈ 2.25 × 10⁸ m/s."
            )
        )
    }

    data class ChemicalEquation(
        val reactants: String,
        val products: String,
        val balancedEquation: String,
        val reactionType: String,
        val keyObservation: String
    )

    fun getChemicalEquations(): List<ChemicalEquation> {
        return listOf(
            ChemicalEquation(
                reactants = "Fe(s) + H₂O(g)",
                products = "Fe₃O₄(s) + H₂(g)",
                balancedEquation = "3Fe(s) + 4H₂O(g) → Fe₃O₄(s) + 4H₂(g)",
                reactionType = "Redox Reaction",
                keyObservation = "Red hot iron reacts with steam to form magnetic oxide of iron and hydrogen gas."
            ),
            ChemicalEquation(
                reactants = "Ca(OH)₂(aq) + CO₂(g)",
                products = "CaCO₃(s) + H₂O(l)",
                balancedEquation = "Ca(OH)₂(aq) + CO₂(g) → CaCO₃(s)↓ + H₂O(l)",
                reactionType = "Precipitation / Carbonation",
                keyObservation = "Limewater turns milky due to the formation of insoluble calcium carbonate."
            ),
            ChemicalEquation(
                reactants = "Pb(NO₃)₂(s) + Heat",
                products = "PbO(s) + NO₂(g) + O₂(g)",
                balancedEquation = "2Pb(NO₃)₂(s) → 2PbO(s) + 4NO₂(g)↑ + O₂(g)↑",
                reactionType = "Thermal Decomposition",
                keyObservation = "Emission of brown fumes of nitrogen dioxide (NO₂) and formation of yellow lead oxide."
            ),
            ChemicalEquation(
                reactants = "CH₄(g) + O₂(g)",
                products = "CO₂(g) + H₂O(l) + Heat",
                balancedEquation = "CH₄(g) + 2O₂(g) → CO₂(g) + 2H₂O(l) + Heat & Light",
                reactionType = "Combustion Reaction",
                keyObservation = "Methane burns in excess air with a clean blue flame producing heat energy."
            ),
            ChemicalEquation(
                reactants = "CH₃COOH + C₂H₅OH (in acid)",
                products = "CH₃COOC₂H₅ + H₂O",
                balancedEquation = "CH₃COOH + C₂H₅OH —(conc. H₂SO₄)→ CH₃COOC₂H₅ + H₂O",
                reactionType = "Esterification",
                keyObservation = "Formation of ethyl ethanoate with a distinct pleasant fruity, sweet smell."
            )
        )
    }
}
