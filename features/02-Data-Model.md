# Data Model

## Enums and other data

### Country

Application will store a list of countries. Country is defined by:

- ID (auto-generated)
- Name (string)

## Golf Course data

About a golf course the application will store:

- Name (string)
- Country (selected from country list)
- Description (string)
- Official rating (float, 0 to 10)
- Personal rating (float, 0 to 10)
- Logo ball (boolean flag whether the player possesses a logo ball from the course)
- Unit for hole length (enum: METERS or YARDS)

### Tee

Each course defines a list of tees. A tee has:

- ID (auto-generated)
- Name (string, e.g. "White", "Red", "Blue")
- Course rating (float) — CR for this tee
- Slope rating (float) — SR for this tee
- Course (reference to parent course)

### Hole

The application will store information about the holes in the course. It will be either 9 or 18 holes per course.

For each hole the application will store:

- ID (auto-generated)
- Number (int, 1–18)
- Par (int, 3–6)
- HCP (int, 1–18)
- Course (reference to parent course)

### Hole Tee Length

For each combination of hole and tee, the length is stored:

- Hole (reference to hole)
- Tee (reference to tee)
- Length (int, in the unit defined on the course)

## Played rounds data

For each round played by the logged-in user the application will store:

- ID (auto-generated)
- Date (date)
- Course (reference to course)
- Tee (reference to tee — the tee played from, selected from tees available at the given course)
- User (reference to user — the logged-in user who played the round)

### Round Score

For each hole played in a round:

- Round (reference to played round)
- Hole (reference to hole entity)
- Score (int, > 0)

## Validation constraints

- Hole number: 1–18 (or 1–9 for 9-hole courses)
- Par: 3–6
- HCP: 1–18
- Score: > 0
- Official rating: 0–10
- Personal rating: 0–10
