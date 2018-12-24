# Contributing to SQL Adapter

First, thanks for taking the time to contribute to SQL Adapter! 🙌🎉 I value
every contribution made to this project, big or small.

## How can I contribute?

There are three big ways you can contribute to this project, without even writing any code.

 1. **Report bugs:** If you encounter a bug, no matter how small, please let me know by [creating an issue](https://github.com/sean0x42/SQLAdapter/issues/new?assignees=sean0x42&labels=bug&template=bug_report.md&title=) with the bug report template.
 2. **Request new features:** Got a good idea to help the project improve? Let me know by [creating a new issue](https://github.com/sean0x42/SQLAdapter/issues/new?assignees=&labels=enhancement&template=feature_request.md&title=) with the feature request template.
 3. **Join in on discussions:** Share your opinion on [open issues](https://github.com/sean0x42/SQLAdapter/issues). Answer questions, further discussion, and add reactions to comments.

If you're hungry for something more substantial, the following is always appreciated as well:

 1. **Fix bugs**: We have a [bug triage project](https://github.com/sean0x42/SQLAdapter/projects/1), which helps track the current situation with known bugs.
 2. **Introduce new features**: But first make sure the feature is related to the current release cycle! If you're unsure, just mention @sean0x42 and ask. Also it's a good idea to check up with anyone else assigned to the issue to make sure someone isn't already working on it.
 3. **Improve the documentation**: There's always room for improvement in documentation. Fix typos, improve grammar, and make any other contributions you can think of.

## How does branching work on this project?

I follow the branching model layed out in [Vincent Driessen's: A successful Git branching model](https://nvie.com/posts/a-successful-git-branching-model/?). The gist of the model is this:

 * **Master:** Contains the current release version of the library.
 * **Develop:** Contains finished features and bug fixes for the next release.
 * **Hotfix:** A contributor made branch, prefixed with `hotfix-`. Contains a bug fix that cannot wait for the next release. Should branch off `master`.
 * **Feature:** A contributor made branch, prefixed with `feature-`. Contains a new feature for the next release. Should branch off `develop`.
 * **Release:** A maintainer made branch, prefixed with `release-`. Contains some minor changes such as version number bumps, and updates to copyright statements. Should branch off `develop` and merge into `master`.
