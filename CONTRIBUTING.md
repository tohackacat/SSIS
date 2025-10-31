# Contributing Guidelines

This document explains how we work on this repo. Everyone follows this. The goal is:
- Keep `main` stable and demoable
- Make it easy to see who did what for grading
- Avoid merge hell right before the deadline

## 1. Branch and task workflow

1. Before starting work, pull the latest `main`:
   - `git checkout main`
   - `git pull`

2. Create a new branch from the updated `main`. Do not work directly on `main`.

3. Do all your coding on that branch.

4. When the task is complete and your branch is up to date with `main`, open a Pull Request (PR). Your code is not considered done until the PR is reviewed and merged.

5. After merge, delete your branch.

Each branch must correspond to a single task / issue. Do not mix multiple tasks in one branch.

## 2. Branch naming

Branch names must follow this format:

`<type>/<short-task-name>`

Allowed types:
- `feature` for new functionality
- `fix` for bug fixes / broken behavior
- `refactor` for reorganizing without changing behavior
- `chore` for non-feature work (docs, tests, cleanup)

Examples:
- `feature/student-model-fields`
- `feature/datastore-add-student`
- `fix/gpa-update`
- `chore/readme-milestone1`

Do not create random branch name patterns. Use one of the allowed types.

## 3. Keeping your branch up to date with main

Main will keep moving while you work. You are responsible for staying in sync.

Any time `main` gets changes that matter to your work (for example, someone added fields to `Student`, changed how GPA is stored, fixed something in the data store), you must pull those changes into your branch.

Before you open a PR, you MUST sync with `main` and resolve conflicts yourself.

Steps to sync:
1. `git checkout main`
2. `git pull`     (this gets the newest commits on main)
3. `git checkout your-branch-name`
4. `git merge main`
5. If there are conflicts, fix them now and commit the merge
6. `git push`

Only after that are you allowed to open your PR

Why this matters:
- If you branched from an old commit (for example off commit 0) and main is now at commit 10, you cannot just open a PR without syncing. Your PR will blow up and will be blocked
- You are required to merge main into your branch and make it work before review. Review is not responsible for cleaning up your conflicts


If you need code from someone else’s branch that isn’t merged yet, do NOT branch off their branch.

Instead:
1. Option A: That person finishes and PRs their branch immediately, we review it, and we merge it into main. Then everyone pulls main and continues.
2. Option B: If only one commit is needed, lead cherry-picks that commit into a small hotfix branch, opens a PR into main, merges it, and everyone pulls main.

Nobody is allowed to build new features on top of somebody else’s in-progress branch. All work must be based on current main.

## 4. Commit message style

We use [Conventional Commit](https://www.conventionalcommits.org/en/v1.0.0/)(https://www.conventionalcommits.org/en/v1.0.0/) style for commit headers.

Format:
`<type>(scope): <short summary>`

`type` should usually match the branch type (`feat` goes with `feature`, etc).

Common types:
- `feat` for a new feature
- `fix` for a bug fix
- `refactor` for internal restructure
- `chore` for docs / cleanup / tests

`scope` is the area you touched (student, datastore, faculty, menu, readme, etc).

Examples:
- `feat(student): add GPA and completedCourses`
- `feat(datastore): support addStudent and listStudents`
- `fix(student): prevent null GPA update`
- `chore(readme): add project description`

Bad commit messages like `update stuff` or `works now` are not allowed

Make multiple small commits instead of dumping one giant everything commit at the end. The commit history in your branch should tell a story.

## 5. Pull Requests

When your task is done and your branch is synced with the latest `main`, open a PR into `main`.

PR title should follow this pattern:
`<branch-name>: short description (closes #ISSUE_NUMBER)`

Examples:
- `feature/student-model-fields: add GPA, completedCourses, graduation credits (closes #12)`
- `feature/datastore-add-student: add addStudent() and listStudents() (closes #15)`

PR description MUST include:
1. What you added or changed
2. Which milestone task / issue this completes
3. Anything important reviewers need to test or check

You are not allowed to merge your own PR unless you are the lead and you are explicitly acting as reviewer for that PR.

## 6. Review and merge rules

Main is protected. You cannot push directly to it.

All changes to main must come from a PR.

Every PR must:
- Be up to date with main before opening
- Have at least one approval
- Pass review feedback

After approval, we squash merge into `main`. Squash merge means all your work becomes one clean commit in `main`. This keeps `main` history readable and ties one commit to one task.

You may NOT continue to pile extra changes onto a PR after it is approved. If you push new commits after approval, GitHub will dismiss the approval and the PR must be re-approved.

## 7. After merge

After your PR is merged:
1. Delete your branch (GitHub offers a delete button)
2. Do not keep working in that old branch
3. For your next task, start a fresh new branch from the latest `main` again

Never stack new work on top of an old branch that was already merged

## 8. Project expectations

`main` must always compile and run

`main` must always be demoable in front of the instructor without crashing on startup

`main` is not a playground

All model classes (Student, Course, Faculty, etc) must:
- Keep fields private
- Expose getters/setters
- Have a readable `toString()`
- Follow whatever naming/field contract is documented by the lead
- Include any required static constants (for example, required credits for graduation)

All menu logic in later milestones will call into the shared data store instead of directly mutating lists. Do not bypass the data store from UI code.

If you break these rules, review will block your PR.
