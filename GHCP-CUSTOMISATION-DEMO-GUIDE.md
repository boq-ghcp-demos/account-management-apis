# GitHub Copilot Customization Demo Guide

## Overview
This guide demonstrates how to customize GitHub Copilot using the `.github` folder structure for agents, instructions, and prompts.

## Structure

```
.github/
├── agents/           # Custom chat agents
├── instructions/     # Code-specific guidelines  
└── prompts/         # Reusable prompt templates
```

## Customization Types

### 1. Custom Agents (`.agent.md`)
Create specialized chat modes in `.github/agents/`:

**Example: [API Architect](.github/agents/api-architect.agent.md)**
```markdown
---
description: 'API architect for design guidance and code generation'
---
# API Architect mode instructions
Your role is to act as an API architect...
```

**Key Features:**
- Domain-specific expertise
- Guided workflows (requires "generate" command)
- Multi-layer architecture patterns

### 2. Instructions (`.instructions.md`)
Define coding standards in `.github/instructions/`:

**Example: [Java Guidelines](.github/instructions/java.instructions.md)**
```markdown
---
description: 'Guidelines for building Java base applications'
applyTo: '**/*.java'
---
```

**Key Features:**
- File pattern targeting (`applyTo`)
- Static analysis integration
- Code quality enforcement

### 3. Prompts (`.prompt.md`)
Create reusable templates in `.github/prompts/`:

**Example: [SQL Review](.github/prompts/sql-code-review.prompt.md)**
```markdown
---
agent: 'agent'
tools: ['changes', 'search/codebase', 'edit/editFiles']
description: 'SQL code review for security and performance'
---
```

**Key Features:**
- Tool specifications
- Security-focused analysis
- Multi-database support

## Quick Start

1. **Create Agent**: Add `.agent.md` file for specialized assistance
2. **Add Instructions**: Create `.instructions.md` for code standards
3. **Build Prompts**: Develop `.prompt.md` for repeatable tasks

## Best Practices

- Keep descriptions concise and actionable
- Use appropriate file patterns (`applyTo`)
- Minimize required tools
- Focus on specific domains/tasks
- Test with target file types

## Usage

- **Agents**: Invoke via `@agent-name` in chat
- **Instructions**: Auto-applied to matching files
- **Prompts**: Access through prompt library or `/prompt-name`