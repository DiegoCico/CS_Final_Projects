const assignment = {
  id: 1,
  title: "NodeJS Assignment",
  description: "Create a NodeJS server with ExpressJS",
  due: "2021-10-10",
  completed: false,
  score: 0,
};

const module = {
  id: "M101",
  name: "Web Development",
  description: "Learn Node and React",
  course: "CS5610",
};

export default function WorkingWithObjects(app) {
  const getAssignment = (req, res) => {
    res.json(assignment);
  };
  const getAssignmentTitle = (req, res) => {
    res.json(assignment.title);
  };
  const updateAssignmentTitle = (req, res) => {
    assignment.title = req.params.title;
    res.json(assignment);
  };
  const updateAssignmentScore = (req, res) => {
    assignment.score = parseInt(req.params.score);
    res.json(assignment);
  };
  const updateAssignmentCompleted = (req, res) => {
    assignment.completed = req.params.completed === "true";
    res.json(assignment);
  };
  const getModule = (req, res) => {
    res.json(module);
  };
  const getModuleName = (req, res) => {
    res.json(module.name);
  };
  const updateModuleName = (req, res) => {
    module.name = req.params.name;
    res.json(module);
  };
  const updateModuleDescription = (req, res) => {
    module.description = req.params.description;
    res.json(module);
  };

  app.get("/lab5/assignment", getAssignment);
  app.get("/lab5/assignment/title", getAssignmentTitle);
  app.get("/lab5/assignment/title/:title", updateAssignmentTitle);
  app.get("/lab5/assignment/score/:score", updateAssignmentScore);
  app.get("/lab5/assignment/completed/:completed", updateAssignmentCompleted);

  app.get("/lab5/module", getModule);
  app.get("/lab5/module/name", getModuleName);
  app.get("/lab5/module/name/:name", updateModuleName);
  app.get("/lab5/module/description/:description", updateModuleDescription);
}
