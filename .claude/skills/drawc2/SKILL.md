---
name: drawc2
description: Draw a C4 Level 2 (Container) diagram, plus a numbered dynamic flow, for a system design from the catalog in src/main/resources/sdcs or for a design the user describes. Use when the user asks to draw, visualise or diagram a system design as C4, a container diagram, or "drawc2".
---

# drawc2: C4 Container diagram

Draw a C4 **Level 2 Container diagram** for one system design, so engineers can see
the runnable pieces, their technologies, and how data moves between them.

## Input

- A catalog slug or name (e.g. `large-file-upload`): read
  `src/main/resources/sdcs/<slug>.json`. If no argument is given, list the files in
  that folder and ask which one.
- Or a design the user describes in chat.

Never invent facts the source does not support. If a relationship or technology is
your own inference, label it as such in the notes (see Output).

## Mapping the catalog JSON to C4

| JSON field | C4 element |
|---|---|
| `name` | Diagram title and the system boundary label |
| `classicExample` | Subtitle ("e.g. Dropbox / Google Drive") |
| each `keyComponents[]` | One **container**: `name`, `[technologies joined by " / "]`, `role` as description |
| `coreBlueprint` | Source for the relationships and the dynamic flow |

Add what a Container diagram needs even though the JSON does not list it:

- **People** using the system (e.g. "User") and the **client** they use (browser or
  app), drawn outside or at the edge of the boundary.
- **External systems** outside the boundary only when the blueprint names them.
- **Relationships**: one labelled arrow per interaction, verb first, with protocol
  when known: `"Uploads parts directly [HTTPS, presigned URL]"`. Derive them from
  `coreBlueprint` and the component roles.

## C4 rules to follow

- A container is something that runs or stores data separately. Not a class, not a
  Docker container necessarily.
- Every box has: name, technology in brackets, one-line responsibility.
- Every arrow has a label. No unlabelled lines, no two-headed arrows.
- Data stores (databases, object storage) use the cylinder shape; queues are labelled
  as queues.
- Show the design's key insight visibly. For example, in large-file-upload the bytes go
  Client → Object Storage directly, and no arrow carries file bytes through the API.
- Keep it to one diagram that fits on a screen: at most ~10 containers.


## Output

Write a static page `src/main/resources/static/sdcs/c4/<slug>.html`. The design page
`/sdcs/<slug>` links to it automatically when the file exists (see
`SystemDesignCatalogController.c4DiagramUrl`). Use
`static/sdcs/c4/large-file-upload.html` as the template: copy its head, header, page
structure and `<style>` block, and change only the content.

The page contains:

1. Back link to `/sdcs/<slug>`, `<h1 class="log-title">` with the design name, and a
   lead line: "C4 Level 2: Container diagram. Classic example: ...".
2. One sentence saying what the diagram's key insight is, and which arrow shows it.
3. The **container diagram** as hand-written inline SVG (`viewBox="0 0 1000 600"`):
   - `<title>` and `<desc>` describing the diagram in words, for screen readers.
   - Dashed boundary rect labelled `<name> [Software System]`.
   - Person (rounded rect, class `person`) and clients (class `external`) outside it.
   - Containers: rect with class `container`; data stores: cylinder (path + ellipse)
     with class `store`. Each has `name`, `[tech]` and one or two `desc` lines.
   - Relationships: `line` or `polyline` with class `rel` and `marker-end="url(#arrow)"`,
     plus a `rel-label`. The key-insight arrow uses `rel key` and `#arrow-key`.
   - Lay boxes out on a grid and route arrows so that **no lines cross** and no label
     overlaps a box or a line. Use elbow `polyline`s for long connections.
     Text at 11px fits about 30 characters per 190px box line.
4. **Upload flow** (or the design's main scenario): an `<ol class="flow">` of numbered
   steps, using the same names as the diagram.
5. **Assumptions**: a list of everything not in the catalog JSON. If none, say so.

Then tell the user the file path, and that they need to restart the app (or rebuild)
for the page and link to appear.

## Rules for this project

- Do not install anything or load JavaScript libraries (see AGENTS.md). The diagram is
  plain inline SVG; the page uses only `/styles.css` and the site's fonts.
- Do not change the catalog JSON or the app's code. This skill only writes the static
  page. If the JSON is missing information the diagram needs, say so and suggest the
  change instead of making it.
- If you do not know something, say "I DO NOT KNOW" rather than guessing.
