---
description: 'Create a pull request from the current branch to main using GitHub MCP server'
agent: 'agent'
tools:
  - mcp_io_github_git
---

# Create Pull Request

Create a pull request from the current branch back to the main branch using the GitHub MCP server.

## Mission

Automatically create a well-formatted pull request that merges changes from the current working branch into the main branch of the repository, with a comprehensive description of changes and appropriate metadata.

## Scope & Preconditions

**Repository Context:**
- Repository: `boq-ghcp-demos/account-management-apis`
- Target Branch: `main`
- Source Branch: Current working branch (determined at runtime)

**Prerequisites:**
- User must be on a branch other than `main`
- Branch must have commits ready to be merged
- User must have write access to the repository
- Changes should be committed to the current branch

## Workflow

### 1. Gather Branch Information
- Determine the current branch name using `git branch --show-current` or equivalent
- If on `main`, stop and inform the user they need to create a feature branch first
- Verify there are commits on the current branch that differ from `main`

### 2. Analyze Changes
- Use `git diff main..HEAD` or similar to understand what has changed
- Identify modified files, additions, and deletions
- Categorize changes by type (features, fixes, documentation, tests, etc.)
- Summarize the scope and purpose of the changes

### 3. Generate Pull Request Content

**Title Format:**
- For feature branches: `feat: [Concise description of feature]`
- For bug fixes: `fix: [Concise description of fix]`
- For documentation: `docs: [Concise description]`
- For tests: `test: [Concise description]`
- For refactoring: `refactor: [Concise description]`
- For chores: `chore: [Concise description]`

**Description Structure:**
```markdown
## Summary
[Brief overview of what this PR accomplishes]

## Changes Made
- [List key changes with bullet points]
- [Be specific about files/features modified]

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Test addition/update
- [ ] Refactoring
- [ ] Configuration change

## Testing
[Describe testing performed or tests added]

## Related Issues
[Reference any related issues or tickets]

## Additional Notes
[Any additional context, deployment notes, or considerations]
```

### 4. Create Pull Request
Use the `mcp_io_github_git_create_pull_request` tool with:
- **owner:** `boq-ghcp-demos`
- **repo:** `account-management-apis`
- **title:** Generated title following convention
- **head:** Current branch name
- **base:** `main`
- **body:** Comprehensive description following template
- **draft:** `false` (unless specified otherwise)

### 5. Confirm Creation
- Display the PR URL
- Provide PR number and status
- Summarize next steps (code review, CI checks, etc.)

## Output Expectations

**Success Response:**
```
✅ Pull Request Created Successfully

PR #[NUMBER]: [TITLE]
URL: https://github.com/boq-ghcp-demos/account-management-apis/pull/[NUMBER]

Branch: [SOURCE_BRANCH] → main
Status: Open

Next Steps:
1. Wait for CI/CD checks to complete
2. Request code review from team members
3. Address any review comments
4. Merge when approved
```

**If on main branch:**
```
❌ Cannot create PR from main branch

You are currently on the 'main' branch. To create a pull request:
1. Create a new feature branch: git checkout -b feature/your-feature-name
2. Make your changes and commit them
3. Run this prompt again to create the PR
```

## Error Handling

- **No differences from main:** Inform user there are no changes to create a PR for
- **GitHub API errors:** Provide clear error messages and potential solutions
- **Permission errors:** Inform user they may lack repository write access
- **Network errors:** Suggest retrying the operation

## Quality Assurance

- [ ] Current branch identified correctly
- [ ] Changes analyzed and categorized appropriately
- [ ] PR title follows conventional commit format
- [ ] Description is comprehensive and well-structured
- [ ] All required tool parameters provided correctly
- [ ] Success/error messages are clear and actionable
- [ ] PR URL is valid and accessible

## Examples

### Example 1: Feature Branch PR
```
Current Branch: feature/playwright-tests
Changes: Added Playwright test suite with TypeScript

Generated PR:
Title: feat: Add Playwright test suite for TravelApp
Description: 
## Summary
Implemented comprehensive Playwright test suite with TypeScript for automated
testing of TravelApp search functionality.

## Changes Made
- Created playwright-tests project with TypeScript configuration
- Implemented Page Object Model for maintainable test structure
- Added tests for Adventure category filtering
- Added tests for price range filtering ($100-$1000)
- Configured Chrome headful mode with HTML reports
- Added comprehensive documentation (README, test summaries)

## Type of Change
- [x] New feature
- [x] Test addition/update
- [x] Documentation update

## Testing
All 11 tests passing (100% success rate):
- 3 Adventure category tests
- 4 Price range tests
- 4 End-to-end journey tests

## Additional Notes
Tests run in headful mode for easy debugging. Screenshots and videos captured
on failure. Complete documentation provided in playwright-tests/README.md.
```

### Example 2: Documentation Update PR
```
Current Branch: docs/update-api-docs
Changes: Updated API documentation

Generated PR:
Title: docs: Update API documentation with new endpoints
Description:
## Summary
Updated API documentation to reflect newly added banking endpoints.

## Changes Made
- Added documentation for account management endpoints
- Updated OpenAPI specification
- Added usage examples for new endpoints

## Type of Change
- [x] Documentation update
```

## Maintenance Guidance

This prompt should be updated when:
- Repository naming or ownership changes
- Branch naming conventions change
- PR template requirements change
- Additional GitHub metadata fields become required
- Organization-specific PR policies change

## Additional Resources

- [GitHub Pull Request Documentation](https://docs.github.com/en/pull-requests)
- [Conventional Commits](https://www.conventionalcommits.org/)
- Repository Contributing Guidelines (if available)
